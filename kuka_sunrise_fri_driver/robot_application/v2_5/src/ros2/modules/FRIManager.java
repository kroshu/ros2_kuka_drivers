package ros2.modules;

import com.kuka.fri.FRIConfiguration;
import com.kuka.fri.FRIJointOverlay;
import com.kuka.fri.FRISession;

import com.kuka.fri.common.ClientCommandMode;
import com.kuka.fri.common.FRISessionState;
import com.kuka.io.IIoDefinitionProvider;
import com.kuka.motion.IMotionContainer;
import com.kuka.device.RoboticArm;
import com.kuka.roboticsAPI.applicationModel.IApplicationControl;
import com.kuka.roboticsAPI.motionModel.ErrorHandlingAction;
import com.kuka.roboticsAPI.motionModel.IMotionErrorHandler;
import com.kuka.roboticsAPI.motionModel.PositionHold;
import com.kuka.roboticsAPI.motionModel.controlModeModel.IMotionControlMode;
import com.kuka.sensitivity.LBR;
import com.kuka.sensitivity.controlmode.JointImpedanceControlMode;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.inject.Inject;
import ros2.serialization.FRIConfigurationParams;

public class FRIManager{
	public enum CommandResult{
		EXECUTED,
		REJECTED,
		ERRORED;
	}
	private ROS2Connection _ROS2Connection;

	private State _currentState;
	private LBR _lbr;
	private FRISession _FRISession;
	private FRIConfiguration _FRIConfiguration;
	private IMotionControlMode _controlMode;
	private ClientCommandMode _clientCommandMode;
	private IMotionContainer _motionContainer;
	private FRIMotionErrorHandler _friMotionErrorHandler = new FRIMotionErrorHandler();
	
	private static double[] stiffness_ = new double[7];
	
    
	public FRIManager(LBR lbr, IApplicationControl applicationControl){
		_currentState = new InactiveState();
		_lbr = lbr;
		_FRISession = null;
		_FRIConfiguration = new FRIConfigurationParams().toFRIConfiguration(_lbr);
		Arrays.fill(stiffness_, 200);
		_controlMode = new JointImpedanceControlMode(stiffness_);
		_clientCommandMode = ClientCommandMode.JOINT_POSITION;
		applicationControl.registerMotionErrorHandler(_friMotionErrorHandler);
	}

	public void registerROS2ConnectionModule(ROS2Connection ros2ConnectionModule){
		_ROS2Connection = ros2ConnectionModule;
	}

	public void close(){
		if(_currentState instanceof ControlActiveState){
			deactivateControl();
		}
		if(_currentState instanceof FRIActiveState){
			endFRI();//TODO: handle error
		}
	}

	public FRIConfigurationParams getFRIConfig(){
		FRIConfigurationParams friConfigurationParams = new FRIConfigurationParams(_FRIConfiguration);
		return friConfigurationParams;
	}

	public ClientCommandMode getClientCommandMode(){
		return _clientCommandMode;
	}

	public IMotionControlMode getControlMode(){
		return _controlMode;
	}

	public FRISessionState getFRISessionState(){
		return _FRISession.getFRIChannelInformation().getFRISessionState();
	}

	/*
	 *  The Following commands change the state of the FRI Manager, and thus are forwarded to the state machine for validation
	 *  */

	public CommandResult setControlMode(IMotionControlMode controlMode){
		return _currentState.setControlMode(controlMode);
	}

	public CommandResult setCommandMode(ClientCommandMode clientCommandMode){
		return _currentState.setCommandMode(clientCommandMode);
	}

	public CommandResult setFRIConfig(FRIConfigurationParams friConfigurationParams){
		return _currentState.setFRIConfig(friConfigurationParams);
	}

	public CommandResult startFRI(){
		CommandResult commandResult = _currentState.startFRI();
		if(commandResult == CommandResult.EXECUTED){
			_currentState = new FRIActiveState();
		}
		return commandResult;
	}

	public CommandResult endFRI(){
		CommandResult commandResult = _currentState.endFRI();
		if(commandResult == CommandResult.EXECUTED){
			_currentState = new InactiveState();
		}
		return commandResult;
	}

	public CommandResult activateControl(){
		CommandResult commandResult = _currentState.activateControl();
		if(commandResult == CommandResult.EXECUTED){
			_currentState = new ControlActiveState();
		}
		return commandResult;
	}

	public CommandResult deactivateControl(){
		CommandResult commandResult = _currentState.deactivateControl();
		if(commandResult == CommandResult.EXECUTED){
			_currentState = new FRIActiveState();
		}
		return commandResult;
	}

	private class State{
		/* By default reject all commands */
		public CommandResult startFRI(){
			return CommandResult.REJECTED;
		}

		public CommandResult endFRI(){
			return CommandResult.REJECTED;
		}

		public CommandResult activateControl(){
			return CommandResult.REJECTED;
		}

		public CommandResult deactivateControl(){
			return CommandResult.REJECTED;
		}

		public CommandResult setFRIConfig(FRIConfigurationParams friConfigurationParams){
			return CommandResult.REJECTED;
		}

		public CommandResult setControlMode(IMotionControlMode controlMode){
			return CommandResult.REJECTED;
		}

		public CommandResult setCommandMode(ClientCommandMode clientCommandMode){
			return CommandResult.REJECTED;
		}
	}

	private class InactiveState extends State{
		@Override
		public CommandResult startFRI(){
			FRIManager.this._FRISession = new FRISession(FRIManager.this._FRIConfiguration);
			try {
				FRIManager.this._FRISession.await(10, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				FRIManager.this._FRISession.close();
				return CommandResult.ERRORED;
			}

			return CommandResult.EXECUTED;
		}
		@Override
		public CommandResult setFRIConfig(FRIConfigurationParams friConfigurationParams){
			FRIManager.this._FRIConfiguration = friConfigurationParams.toFRIConfiguration(FRIManager.this._lbr);
			return CommandResult.EXECUTED;
		}
		@Override
		public CommandResult setControlMode(IMotionControlMode controlMode){
			FRIManager.this._controlMode = controlMode;
			return CommandResult.EXECUTED;
		}
		@Override
		public CommandResult setCommandMode(ClientCommandMode clientCommandMode){
			FRIManager.this._clientCommandMode = clientCommandMode;
			return CommandResult.EXECUTED;
		}
	}

	private class FRIActiveState extends State {
		@Override
		public CommandResult endFRI(){
			try{
				FRIManager.this._FRISession.close();
			} catch(IllegalStateException e){
				return CommandResult.ERRORED;
			}
			return CommandResult.EXECUTED;
		}
		@Override
		public CommandResult activateControl(){
		  System.out.println("ClientCommandMode: " + FRIManager.this._clientCommandMode + ".");
		  if (FRIManager.this._clientCommandMode==ClientCommandMode.CARTESIAN_POSE) {
		    FRICartesianOverlay friCartesianOverlay =
          new FRICartesianOverlay(FRIManager.this._FRISession);
		    PositionHold motion =
	          new PositionHold(FRIManager.this._controlMode, -1, null);
	        FRIManager.this._motionContainer =
	          FRIManager.this._lbr.getFlange().moveAsync(motion.addMotionOverlay(friCartesianOverlay));
		  }
		  else {
		    FRIJointOverlay friJointOverlay =
					new FRIJointOverlay(FRIManager.this._FRISession, FRIManager.this._clientCommandMode);
		    PositionHold motion =
					new PositionHold(FRIManager.this._controlMode, -1, null);
		    FRIManager.this._motionContainer =
					FRIManager.this._lbr.getFlange().moveAsync(motion.addMotionOverlay(friJointOverlay));
		  }
			return CommandResult.EXECUTED;
		}
		@Override
		public CommandResult setControlMode(IMotionControlMode controlMode){
			FRIManager.this._controlMode = controlMode;
			return CommandResult.EXECUTED;
		}
		@Override
		public CommandResult setCommandMode(ClientCommandMode clientCommandMode){
			FRIManager.this._clientCommandMode = clientCommandMode;
			return CommandResult.EXECUTED;
		}
	}

	private class ControlActiveState extends State {
		@Override
		public CommandResult deactivateControl(){
			FRIManager.this._motionContainer.cancel();
			return CommandResult.EXECUTED;
		}
	}

	private class FRIMotionErrorHandler implements IMotionErrorHandler{

		@Override
		public ErrorHandlingAction handleExecutionError(
				IMotionContainer failedContainer,
				List<IMotionContainer> canceledContainers) {
			FRISessionState  sessionState = _FRISession.getFRIChannelInformation().getFRISessionState();
			switch(sessionState){
			case IDLE:
				FRIManager.this._ROS2Connection.handleFRIEndedError();
				break;
			default:
				FRIManager.this._ROS2Connection.handleControlEndedError();
  				break;
			}
			System.out.println("Failed container: " + failedContainer.toString() + ".");
			System.out.println("Error: " + failedContainer.getErrorMessage());
			System.out.println("Runtime data: " + failedContainer.getRuntimeData());
			System.out.println("Cancelled containers: " + canceledContainers.toString());
			return ErrorHandlingAction.IGNORE;
		}
		@Override
    public ErrorHandlingAction handleMaintainingError(IMotionContainer lastContainer,
    List<IMotionContainer> canceledContainers,
    String errorMessage) {
      FRISessionState  sessionState = _FRISession.getFRIChannelInformation().getFRISessionState();
      switch(sessionState){
      case IDLE:
        FRIManager.this._ROS2Connection.handleFRIEndedError();
        break;
      default:
        FRIManager.this._ROS2Connection.handleControlEndedError();
        break;
      }
      System.out.println("Last container: " + lastContainer.toString() + ".");
      System.out.println("Error: " + lastContainer.getErrorMessage());
      System.out.println("Runtime data: " + lastContainer.getRuntimeData());
      System.out.println("Cancelled containers: " + canceledContainers.toString());
      return ErrorHandlingAction.IGNORE;
    }

	}

}
