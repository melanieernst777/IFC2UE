// Copyright Epic Games, Inc. All Rights Reserved.
/*===========================================================================
	Generated code exported from UnrealHeaderTool.
	DO NOT modify this manually! Edit the corresponding .h files instead!
===========================================================================*/

#include "UObject/ObjectMacros.h"
#include "UObject/ScriptMacros.h"

PRAGMA_DISABLE_DEPRECATION_WARNINGS
class UStaticMeshComponent;
class UPrimitiveComponent;
struct FKey;
class AActor;
struct FHitResult;
#ifdef IFC2UE_Building_generated_h
#error "Building.generated.h already included, missing '#pragma once' in Building.h"
#endif
#define IFC2UE_Building_generated_h

#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_SPARSE_DATA
#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_RPC_WRAPPERS \
 \
	DECLARE_FUNCTION(execAddLightToComponent); \
	DECLARE_FUNCTION(execComponentClicked); \
	DECLARE_FUNCTION(execOnOverlapEnd); \
	DECLARE_FUNCTION(execOnOverlapBegin);


#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_RPC_WRAPPERS_NO_PURE_DECLS \
 \
	DECLARE_FUNCTION(execAddLightToComponent); \
	DECLARE_FUNCTION(execComponentClicked); \
	DECLARE_FUNCTION(execOnOverlapEnd); \
	DECLARE_FUNCTION(execOnOverlapBegin);


#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_INCLASS_NO_PURE_DECLS \
private: \
	static void StaticRegisterNativesABuilding(); \
	friend struct Z_Construct_UClass_ABuilding_Statics; \
public: \
	DECLARE_CLASS(ABuilding, AActor, COMPILED_IN_FLAGS(0 | CLASS_Config), CASTCLASS_None, TEXT("/Script/IFC2UE"), NO_API) \
	DECLARE_SERIALIZER(ABuilding)


#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_INCLASS \
private: \
	static void StaticRegisterNativesABuilding(); \
	friend struct Z_Construct_UClass_ABuilding_Statics; \
public: \
	DECLARE_CLASS(ABuilding, AActor, COMPILED_IN_FLAGS(0 | CLASS_Config), CASTCLASS_None, TEXT("/Script/IFC2UE"), NO_API) \
	DECLARE_SERIALIZER(ABuilding)


#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_STANDARD_CONSTRUCTORS \
	/** Standard constructor, called after all reflected properties have been initialized */ \
	NO_API ABuilding(const FObjectInitializer& ObjectInitializer); \
	DEFINE_DEFAULT_OBJECT_INITIALIZER_CONSTRUCTOR_CALL(ABuilding) \
	DECLARE_VTABLE_PTR_HELPER_CTOR(NO_API, ABuilding); \
	DEFINE_VTABLE_PTR_HELPER_CTOR_CALLER(ABuilding); \
private: \
	/** Private move- and copy-constructors, should never be used */ \
	NO_API ABuilding(ABuilding&&); \
	NO_API ABuilding(const ABuilding&); \
public:


#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_ENHANCED_CONSTRUCTORS \
private: \
	/** Private move- and copy-constructors, should never be used */ \
	NO_API ABuilding(ABuilding&&); \
	NO_API ABuilding(const ABuilding&); \
public: \
	DECLARE_VTABLE_PTR_HELPER_CTOR(NO_API, ABuilding); \
	DEFINE_VTABLE_PTR_HELPER_CTOR_CALLER(ABuilding); \
	DEFINE_DEFAULT_CONSTRUCTOR_CALL(ABuilding)


#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_14_PROLOG
#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_GENERATED_BODY_LEGACY \
PRAGMA_DISABLE_DEPRECATION_WARNINGS \
public: \
	FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_SPARSE_DATA \
	FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_RPC_WRAPPERS \
	FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_INCLASS \
	FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_STANDARD_CONSTRUCTORS \
public: \
PRAGMA_ENABLE_DEPRECATION_WARNINGS


#define FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_GENERATED_BODY \
PRAGMA_DISABLE_DEPRECATION_WARNINGS \
public: \
	FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_SPARSE_DATA \
	FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_RPC_WRAPPERS_NO_PURE_DECLS \
	FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_INCLASS_NO_PURE_DECLS \
	FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_17_ENHANCED_CONSTRUCTORS \
private: \
PRAGMA_ENABLE_DEPRECATION_WARNINGS


template<> IFC2UE_API UClass* StaticClass<class ABuilding>();

#undef CURRENT_FILE_ID
#define CURRENT_FILE_ID FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h


PRAGMA_ENABLE_DEPRECATION_WARNINGS
