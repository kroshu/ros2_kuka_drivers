// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: kuka/motion/external/external_control_mode.proto

#ifndef GOOGLE_PROTOBUF_INCLUDED_kuka_2fmotion_2fexternal_2fexternal_5fcontrol_5fmode_2eproto
#define GOOGLE_PROTOBUF_INCLUDED_kuka_2fmotion_2fexternal_2fexternal_5fcontrol_5fmode_2eproto

#include <limits>
#include <string>

#include <google/protobuf/port_def.inc>
#if PROTOBUF_VERSION < 3012000
#error This file was generated by a newer version of protoc which is
#error incompatible with your Protocol Buffer headers. Please update
#error your headers.
#endif
#if 3012004 < PROTOBUF_MIN_PROTOC_VERSION
#error This file was generated by an older version of protoc which is
#error incompatible with your Protocol Buffer headers. Please
#error regenerate this file with a newer version of protoc.
#endif

#include <google/protobuf/port_undef.inc>
#include <google/protobuf/io/coded_stream.h>
#include <google/protobuf/arena.h>
#include <google/protobuf/arenastring.h>
#include <google/protobuf/generated_message_table_driven.h>
#include <google/protobuf/generated_message_util.h>
#include <google/protobuf/inlined_string_field.h>
#include <google/protobuf/metadata_lite.h>
#include <google/protobuf/generated_message_reflection.h>
#include <google/protobuf/repeated_field.h>  // IWYU pragma: export
#include <google/protobuf/extension_set.h>  // IWYU pragma: export
#include <google/protobuf/generated_enum_reflection.h>
#include <google/protobuf/any.pb.h>
#include <google/protobuf/wrappers.pb.h>
// @@protoc_insertion_point(includes)
#include <google/protobuf/port_def.inc>
#define PROTOBUF_INTERNAL_EXPORT_kuka_2fmotion_2fexternal_2fexternal_5fcontrol_5fmode_2eproto
PROTOBUF_NAMESPACE_OPEN
namespace internal {
class AnyMetadata;
}  // namespace internal
PROTOBUF_NAMESPACE_CLOSE

// Internal implementation detail -- do not use these members.
struct TableStruct_kuka_2fmotion_2fexternal_2fexternal_5fcontrol_5fmode_2eproto {
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTableField entries[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::AuxillaryParseTableField aux[]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::ParseTable schema[1]
    PROTOBUF_SECTION_VARIABLE(protodesc_cold);
  static const ::PROTOBUF_NAMESPACE_ID::internal::FieldMetadata field_metadata[];
  static const ::PROTOBUF_NAMESPACE_ID::internal::SerializationTable serialization_table[];
  static const ::PROTOBUF_NAMESPACE_ID::uint32 offsets[];
};
extern const ::PROTOBUF_NAMESPACE_ID::internal::DescriptorTable descriptor_table_kuka_2fmotion_2fexternal_2fexternal_5fcontrol_5fmode_2eproto;
PROTOBUF_NAMESPACE_OPEN
PROTOBUF_NAMESPACE_CLOSE
namespace kuka {
namespace motion {
namespace external {

std::string control_mode_string;

enum ExternalControlMode : int {
  EXTERNAL_CONTROL_MODE_UNSPECIFIED = 0,
  POSITION_CONTROL = 1,
  CARTESIAN_OVERLAY = 2,
  JOINT_IMPEDANCE_CONTROL = 3,
  CARTESIAN_IMPEDANCE_CONTROL = 4,
  TORQUE_CONTROL = 5,
  MR_VELOCITY_CONTROL = 6,
  ExternalControlMode_INT_MIN_SENTINEL_DO_NOT_USE_ = std::numeric_limits<::PROTOBUF_NAMESPACE_ID::int32>::min(),
  ExternalControlMode_INT_MAX_SENTINEL_DO_NOT_USE_ = std::numeric_limits<::PROTOBUF_NAMESPACE_ID::int32>::max()
};
bool ExternalControlMode_IsValid(int value);
constexpr ExternalControlMode ExternalControlMode_MIN = EXTERNAL_CONTROL_MODE_UNSPECIFIED;
constexpr ExternalControlMode ExternalControlMode_MAX = MR_VELOCITY_CONTROL;
constexpr int ExternalControlMode_ARRAYSIZE = ExternalControlMode_MAX + 1;

const ::PROTOBUF_NAMESPACE_ID::EnumDescriptor* ExternalControlMode_descriptor();
template<typename T>
inline const std::string& ExternalControlMode_Name(T enum_t_value) {
  static_assert(::std::is_same<T, ExternalControlMode>::value ||
    ::std::is_integral<T>::value,
    "Incorrect type passed to function ExternalControlMode_Name.");
  switch(enum_t_value)
  {
    case ExternalControlMode::POSITION_CONTROL:
      control_mode_string = "POSITION_CONTROL";
      break;
    case ExternalControlMode::CARTESIAN_OVERLAY:
      control_mode_string = "CARTESIAN_OVERLAY";
      break;
    case ExternalControlMode::JOINT_IMPEDANCE_CONTROL:
      control_mode_string = "JOINT_IMPEDANCE_CONTROL";
      break;
    case ExternalControlMode::CARTESIAN_IMPEDANCE_CONTROL:
      control_mode_string = "CARTESIAN_IMPEDANCE_CONTROL";
      break;
    case ExternalControlMode::TORQUE_CONTROL:
      control_mode_string = "TORQUE_CONTROL";
      break;
    case ExternalControlMode::MR_VELOCITY_CONTROL:
      control_mode_string = "MR_VELOCITY_CONTROL";
      break;
    default:
     control_mode_string = "EXTERNAL_CONTROL_MODE_UNSPECIFIED";
      break;
  }
  return control_mode_string;
}
inline bool ExternalControlMode_Parse(
    const std::string& name, ExternalControlMode* value) {
  return ::PROTOBUF_NAMESPACE_ID::internal::ParseNamedEnum<ExternalControlMode>(
    ExternalControlMode_descriptor(), name, value);
}
// ===================================================================


// ===================================================================


// ===================================================================

#ifdef __GNUC__
  #pragma GCC diagnostic push
  #pragma GCC diagnostic ignored "-Wstrict-aliasing"
#endif  // __GNUC__
#ifdef __GNUC__
  #pragma GCC diagnostic pop
#endif  // __GNUC__

// @@protoc_insertion_point(namespace_scope)

}  // namespace external
}  // namespace motion
}  // namespace kuka

PROTOBUF_NAMESPACE_OPEN

template <> struct is_proto_enum< ::kuka::motion::external::ExternalControlMode> : ::std::true_type {};
template <>
inline const EnumDescriptor* GetEnumDescriptor< ::kuka::motion::external::ExternalControlMode>() {
  return ::kuka::motion::external::ExternalControlMode_descriptor();
}

PROTOBUF_NAMESPACE_CLOSE

// @@protoc_insertion_point(global_scope)

#include <google/protobuf/port_undef.inc>
#endif  // GOOGLE_PROTOBUF_INCLUDED_GOOGLE_PROTOBUF_INCLUDED_kuka_2fmotion_2fexternal_2fexternal_5fcontrol_5fmode_2eproto
