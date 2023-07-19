// Copyright Epic Games, Inc. All Rights Reserved.
/*===========================================================================
	Generated code exported from UnrealHeaderTool.
	DO NOT modify this manually! Edit the corresponding .h files instead!
===========================================================================*/

#include "UObject/GeneratedCppIncludes.h"
#include "IFC2UE/Public/Building.h"
PRAGMA_DISABLE_DEPRECATION_WARNINGS
void EmptyLinkFunctionForGeneratedCodeBuilding() {}
// Cross Module References
	IFC2UE_API UClass* Z_Construct_UClass_ABuilding_NoRegister();
	IFC2UE_API UClass* Z_Construct_UClass_ABuilding();
	ENGINE_API UClass* Z_Construct_UClass_AActor();
	UPackage* Z_Construct_UPackage__Script_IFC2UE();
	ENGINE_API UClass* Z_Construct_UClass_UStaticMeshComponent_NoRegister();
	COREUOBJECT_API UScriptStruct* Z_Construct_UScriptStruct_FVector();
	ENGINE_API UClass* Z_Construct_UClass_UPrimitiveComponent_NoRegister();
	INPUTCORE_API UScriptStruct* Z_Construct_UScriptStruct_FKey();
	ENGINE_API UClass* Z_Construct_UClass_AActor_NoRegister();
	ENGINE_API UScriptStruct* Z_Construct_UScriptStruct_FHitResult();
	ENGINE_API UClass* Z_Construct_UClass_UPointLightComponent_NoRegister();
	ENGINE_API UClass* Z_Construct_UClass_USphereComponent_NoRegister();
	ENGINE_API UClass* Z_Construct_UClass_USceneComponent_NoRegister();
	COREUOBJECT_API UClass* Z_Construct_UClass_UClass();
// End Cross Module References
	DEFINE_FUNCTION(ABuilding::execAddLightToComponent)
	{
		P_GET_OBJECT(UStaticMeshComponent,Z_Param_Component);
		P_GET_STRUCT(FVector,Z_Param_LightLocation);
		P_GET_STRUCT(FVector,Z_Param_SphereLocation);
		P_FINISH;
		P_NATIVE_BEGIN;
		P_THIS->AddLightToComponent(Z_Param_Component,Z_Param_LightLocation,Z_Param_SphereLocation);
		P_NATIVE_END;
	}
	DEFINE_FUNCTION(ABuilding::execComponentClicked)
	{
		P_GET_OBJECT(UPrimitiveComponent,Z_Param_PrimitiveComponent);
		P_GET_STRUCT(FKey,Z_Param_Key);
		P_FINISH;
		P_NATIVE_BEGIN;
		P_THIS->ComponentClicked(Z_Param_PrimitiveComponent,Z_Param_Key);
		P_NATIVE_END;
	}
	DEFINE_FUNCTION(ABuilding::execOnOverlapEnd)
	{
		P_GET_OBJECT(UPrimitiveComponent,Z_Param_OverlappedComp);
		P_GET_OBJECT(AActor,Z_Param_OtherActor);
		P_GET_OBJECT(UPrimitiveComponent,Z_Param_OtherComp);
		P_GET_PROPERTY(FIntProperty,Z_Param_OtherBodyIndex);
		P_FINISH;
		P_NATIVE_BEGIN;
		P_THIS->OnOverlapEnd(Z_Param_OverlappedComp,Z_Param_OtherActor,Z_Param_OtherComp,Z_Param_OtherBodyIndex);
		P_NATIVE_END;
	}
	DEFINE_FUNCTION(ABuilding::execOnOverlapBegin)
	{
		P_GET_OBJECT(UPrimitiveComponent,Z_Param_OverlappedComp);
		P_GET_OBJECT(AActor,Z_Param_OtherActor);
		P_GET_OBJECT(UPrimitiveComponent,Z_Param_OtherComp);
		P_GET_PROPERTY(FIntProperty,Z_Param_OtherBodyIndex);
		P_GET_UBOOL(Z_Param_bFromSweep);
		P_GET_STRUCT_REF(FHitResult,Z_Param_Out_SweepResult);
		P_FINISH;
		P_NATIVE_BEGIN;
		P_THIS->OnOverlapBegin(Z_Param_OverlappedComp,Z_Param_OtherActor,Z_Param_OtherComp,Z_Param_OtherBodyIndex,Z_Param_bFromSweep,Z_Param_Out_SweepResult);
		P_NATIVE_END;
	}
	void ABuilding::StaticRegisterNativesABuilding()
	{
		UClass* Class = ABuilding::StaticClass();
		static const FNameNativePtrPair Funcs[] = {
			{ "AddLightToComponent", &ABuilding::execAddLightToComponent },
			{ "ComponentClicked", &ABuilding::execComponentClicked },
			{ "OnOverlapBegin", &ABuilding::execOnOverlapBegin },
			{ "OnOverlapEnd", &ABuilding::execOnOverlapEnd },
		};
		FNativeFunctionRegistrar::RegisterFunctions(Class, Funcs, UE_ARRAY_COUNT(Funcs));
	}
	struct Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics
	{
		struct Building_eventAddLightToComponent_Parms
		{
			UStaticMeshComponent* Component;
			FVector LightLocation;
			FVector SphereLocation;
		};
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_Component_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_Component;
		static const UECodeGen_Private::FStructPropertyParams NewProp_LightLocation;
		static const UECodeGen_Private::FStructPropertyParams NewProp_SphereLocation;
		static const UECodeGen_Private::FPropertyParamsBase* const PropPointers[];
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam Function_MetaDataParams[];
#endif
		static const UECodeGen_Private::FFunctionParams FuncParams;
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_Component_MetaData[] = {
		{ "EditInline", "true" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_Component = { "Component", nullptr, (EPropertyFlags)0x0010000000080080, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventAddLightToComponent_Parms, Component), Z_Construct_UClass_UStaticMeshComponent_NoRegister, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_Component_MetaData, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_Component_MetaData)) };
	const UECodeGen_Private::FStructPropertyParams Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_LightLocation = { "LightLocation", nullptr, (EPropertyFlags)0x0010000000000080, UECodeGen_Private::EPropertyGenFlags::Struct, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventAddLightToComponent_Parms, LightLocation), Z_Construct_UScriptStruct_FVector, METADATA_PARAMS(nullptr, 0) };
	const UECodeGen_Private::FStructPropertyParams Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_SphereLocation = { "SphereLocation", nullptr, (EPropertyFlags)0x0010000000000080, UECodeGen_Private::EPropertyGenFlags::Struct, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventAddLightToComponent_Parms, SphereLocation), Z_Construct_UScriptStruct_FVector, METADATA_PARAMS(nullptr, 0) };
	const UECodeGen_Private::FPropertyParamsBase* const Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::PropPointers[] = {
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_Component,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_LightLocation,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::NewProp_SphereLocation,
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::Function_MetaDataParams[] = {
		{ "ModuleRelativePath", "Public/Building.h" },
	};
#endif
	const UECodeGen_Private::FFunctionParams Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::FuncParams = { (UObject*(*)())Z_Construct_UClass_ABuilding, nullptr, "AddLightToComponent", nullptr, nullptr, sizeof(Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::Building_eventAddLightToComponent_Parms), Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::PropPointers, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::PropPointers), RF_Public|RF_Transient|RF_MarkAsNative, (EFunctionFlags)0x00820401, 0, 0, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::Function_MetaDataParams, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::Function_MetaDataParams)) };
	UFunction* Z_Construct_UFunction_ABuilding_AddLightToComponent()
	{
		static UFunction* ReturnFunction = nullptr;
		if (!ReturnFunction)
		{
			UECodeGen_Private::ConstructUFunction(&ReturnFunction, Z_Construct_UFunction_ABuilding_AddLightToComponent_Statics::FuncParams);
		}
		return ReturnFunction;
	}
	struct Z_Construct_UFunction_ABuilding_ComponentClicked_Statics
	{
		struct Building_eventComponentClicked_Parms
		{
			UPrimitiveComponent* PrimitiveComponent;
			FKey Key;
		};
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_PrimitiveComponent_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_PrimitiveComponent;
		static const UECodeGen_Private::FStructPropertyParams NewProp_Key;
		static const UECodeGen_Private::FPropertyParamsBase* const PropPointers[];
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam Function_MetaDataParams[];
#endif
		static const UECodeGen_Private::FFunctionParams FuncParams;
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::NewProp_PrimitiveComponent_MetaData[] = {
		{ "EditInline", "true" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::NewProp_PrimitiveComponent = { "PrimitiveComponent", nullptr, (EPropertyFlags)0x0010000000080080, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventComponentClicked_Parms, PrimitiveComponent), Z_Construct_UClass_UPrimitiveComponent_NoRegister, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::NewProp_PrimitiveComponent_MetaData, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::NewProp_PrimitiveComponent_MetaData)) };
	const UECodeGen_Private::FStructPropertyParams Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::NewProp_Key = { "Key", nullptr, (EPropertyFlags)0x0010000000000080, UECodeGen_Private::EPropertyGenFlags::Struct, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventComponentClicked_Parms, Key), Z_Construct_UScriptStruct_FKey, METADATA_PARAMS(nullptr, 0) }; // 2615338182
	const UECodeGen_Private::FPropertyParamsBase* const Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::PropPointers[] = {
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::NewProp_PrimitiveComponent,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::NewProp_Key,
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::Function_MetaDataParams[] = {
		{ "ModuleRelativePath", "Public/Building.h" },
	};
#endif
	const UECodeGen_Private::FFunctionParams Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::FuncParams = { (UObject*(*)())Z_Construct_UClass_ABuilding, nullptr, "ComponentClicked", nullptr, nullptr, sizeof(Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::Building_eventComponentClicked_Parms), Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::PropPointers, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::PropPointers), RF_Public|RF_Transient|RF_MarkAsNative, (EFunctionFlags)0x00020401, 0, 0, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::Function_MetaDataParams, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::Function_MetaDataParams)) };
	UFunction* Z_Construct_UFunction_ABuilding_ComponentClicked()
	{
		static UFunction* ReturnFunction = nullptr;
		if (!ReturnFunction)
		{
			UECodeGen_Private::ConstructUFunction(&ReturnFunction, Z_Construct_UFunction_ABuilding_ComponentClicked_Statics::FuncParams);
		}
		return ReturnFunction;
	}
	struct Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics
	{
		struct Building_eventOnOverlapBegin_Parms
		{
			UPrimitiveComponent* OverlappedComp;
			AActor* OtherActor;
			UPrimitiveComponent* OtherComp;
			int32 OtherBodyIndex;
			bool bFromSweep;
			FHitResult SweepResult;
		};
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_OverlappedComp_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_OverlappedComp;
		static const UECodeGen_Private::FObjectPropertyParams NewProp_OtherActor;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_OtherComp_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_OtherComp;
		static const UECodeGen_Private::FIntPropertyParams NewProp_OtherBodyIndex;
		static void NewProp_bFromSweep_SetBit(void* Obj);
		static const UECodeGen_Private::FBoolPropertyParams NewProp_bFromSweep;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_SweepResult_MetaData[];
#endif
		static const UECodeGen_Private::FStructPropertyParams NewProp_SweepResult;
		static const UECodeGen_Private::FPropertyParamsBase* const PropPointers[];
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam Function_MetaDataParams[];
#endif
		static const UECodeGen_Private::FFunctionParams FuncParams;
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OverlappedComp_MetaData[] = {
		{ "EditInline", "true" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OverlappedComp = { "OverlappedComp", nullptr, (EPropertyFlags)0x0010000000080080, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapBegin_Parms, OverlappedComp), Z_Construct_UClass_UPrimitiveComponent_NoRegister, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OverlappedComp_MetaData, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OverlappedComp_MetaData)) };
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherActor = { "OtherActor", nullptr, (EPropertyFlags)0x0010000000000080, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapBegin_Parms, OtherActor), Z_Construct_UClass_AActor_NoRegister, METADATA_PARAMS(nullptr, 0) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherComp_MetaData[] = {
		{ "EditInline", "true" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherComp = { "OtherComp", nullptr, (EPropertyFlags)0x0010000000080080, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapBegin_Parms, OtherComp), Z_Construct_UClass_UPrimitiveComponent_NoRegister, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherComp_MetaData, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherComp_MetaData)) };
	const UECodeGen_Private::FIntPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherBodyIndex = { "OtherBodyIndex", nullptr, (EPropertyFlags)0x0010000000000080, UECodeGen_Private::EPropertyGenFlags::Int, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapBegin_Parms, OtherBodyIndex), METADATA_PARAMS(nullptr, 0) };
	void Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_bFromSweep_SetBit(void* Obj)
	{
		((Building_eventOnOverlapBegin_Parms*)Obj)->bFromSweep = 1;
	}
	const UECodeGen_Private::FBoolPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_bFromSweep = { "bFromSweep", nullptr, (EPropertyFlags)0x0010000000000080, UECodeGen_Private::EPropertyGenFlags::Bool | UECodeGen_Private::EPropertyGenFlags::NativeBool, RF_Public|RF_Transient|RF_MarkAsNative, 1, sizeof(bool), sizeof(Building_eventOnOverlapBegin_Parms), &Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_bFromSweep_SetBit, METADATA_PARAMS(nullptr, 0) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_SweepResult_MetaData[] = {
		{ "NativeConst", "" },
	};
#endif
	const UECodeGen_Private::FStructPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_SweepResult = { "SweepResult", nullptr, (EPropertyFlags)0x0010008008000182, UECodeGen_Private::EPropertyGenFlags::Struct, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapBegin_Parms, SweepResult), Z_Construct_UScriptStruct_FHitResult, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_SweepResult_MetaData, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_SweepResult_MetaData)) }; // 1416937132
	const UECodeGen_Private::FPropertyParamsBase* const Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::PropPointers[] = {
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OverlappedComp,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherActor,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherComp,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_OtherBodyIndex,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_bFromSweep,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::NewProp_SweepResult,
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::Function_MetaDataParams[] = {
		{ "Comment", "// declare overlap begin function\n" },
		{ "ModuleRelativePath", "Public/Building.h" },
		{ "ToolTip", "declare overlap begin function" },
	};
#endif
	const UECodeGen_Private::FFunctionParams Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::FuncParams = { (UObject*(*)())Z_Construct_UClass_ABuilding, nullptr, "OnOverlapBegin", nullptr, nullptr, sizeof(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::Building_eventOnOverlapBegin_Parms), Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::PropPointers, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::PropPointers), RF_Public|RF_Transient|RF_MarkAsNative, (EFunctionFlags)0x00420401, 0, 0, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::Function_MetaDataParams, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::Function_MetaDataParams)) };
	UFunction* Z_Construct_UFunction_ABuilding_OnOverlapBegin()
	{
		static UFunction* ReturnFunction = nullptr;
		if (!ReturnFunction)
		{
			UECodeGen_Private::ConstructUFunction(&ReturnFunction, Z_Construct_UFunction_ABuilding_OnOverlapBegin_Statics::FuncParams);
		}
		return ReturnFunction;
	}
	struct Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics
	{
		struct Building_eventOnOverlapEnd_Parms
		{
			UPrimitiveComponent* OverlappedComp;
			AActor* OtherActor;
			UPrimitiveComponent* OtherComp;
			int32 OtherBodyIndex;
		};
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_OverlappedComp_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_OverlappedComp;
		static const UECodeGen_Private::FObjectPropertyParams NewProp_OtherActor;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_OtherComp_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_OtherComp;
		static const UECodeGen_Private::FIntPropertyParams NewProp_OtherBodyIndex;
		static const UECodeGen_Private::FPropertyParamsBase* const PropPointers[];
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam Function_MetaDataParams[];
#endif
		static const UECodeGen_Private::FFunctionParams FuncParams;
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OverlappedComp_MetaData[] = {
		{ "EditInline", "true" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OverlappedComp = { "OverlappedComp", nullptr, (EPropertyFlags)0x0010000000080080, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapEnd_Parms, OverlappedComp), Z_Construct_UClass_UPrimitiveComponent_NoRegister, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OverlappedComp_MetaData, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OverlappedComp_MetaData)) };
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherActor = { "OtherActor", nullptr, (EPropertyFlags)0x0010000000000080, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapEnd_Parms, OtherActor), Z_Construct_UClass_AActor_NoRegister, METADATA_PARAMS(nullptr, 0) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherComp_MetaData[] = {
		{ "EditInline", "true" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherComp = { "OtherComp", nullptr, (EPropertyFlags)0x0010000000080080, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapEnd_Parms, OtherComp), Z_Construct_UClass_UPrimitiveComponent_NoRegister, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherComp_MetaData, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherComp_MetaData)) };
	const UECodeGen_Private::FIntPropertyParams Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherBodyIndex = { "OtherBodyIndex", nullptr, (EPropertyFlags)0x0010000000000080, UECodeGen_Private::EPropertyGenFlags::Int, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(Building_eventOnOverlapEnd_Parms, OtherBodyIndex), METADATA_PARAMS(nullptr, 0) };
	const UECodeGen_Private::FPropertyParamsBase* const Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::PropPointers[] = {
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OverlappedComp,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherActor,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherComp,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::NewProp_OtherBodyIndex,
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::Function_MetaDataParams[] = {
		{ "Comment", "// declare overlap end function\n" },
		{ "ModuleRelativePath", "Public/Building.h" },
		{ "ToolTip", "declare overlap end function" },
	};
#endif
	const UECodeGen_Private::FFunctionParams Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::FuncParams = { (UObject*(*)())Z_Construct_UClass_ABuilding, nullptr, "OnOverlapEnd", nullptr, nullptr, sizeof(Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::Building_eventOnOverlapEnd_Parms), Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::PropPointers, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::PropPointers), RF_Public|RF_Transient|RF_MarkAsNative, (EFunctionFlags)0x00020401, 0, 0, METADATA_PARAMS(Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::Function_MetaDataParams, UE_ARRAY_COUNT(Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::Function_MetaDataParams)) };
	UFunction* Z_Construct_UFunction_ABuilding_OnOverlapEnd()
	{
		static UFunction* ReturnFunction = nullptr;
		if (!ReturnFunction)
		{
			UECodeGen_Private::ConstructUFunction(&ReturnFunction, Z_Construct_UFunction_ABuilding_OnOverlapEnd_Statics::FuncParams);
		}
		return ReturnFunction;
	}
	IMPLEMENT_CLASS_NO_AUTO_REGISTRATION(ABuilding);
	UClass* Z_Construct_UClass_ABuilding_NoRegister()
	{
		return ABuilding::StaticClass();
	}
	struct Z_Construct_UClass_ABuilding_Statics
	{
		static UObject* (*const DependentSingletons[])();
		static const FClassFunctionLinkInfo FuncInfo[];
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam Class_MetaDataParams[];
#endif
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_PathToMeshes_MetaData[];
#endif
		static const UECodeGen_Private::FStrPropertyParams NewProp_PathToMeshes;
		static const UECodeGen_Private::FObjectPropertyParams NewProp_LightsAndSwitches_ValueProp;
		static const UECodeGen_Private::FObjectPropertyParams NewProp_LightsAndSwitches_Key_KeyProp;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_LightsAndSwitches_MetaData[];
#endif
		static const UECodeGen_Private::FMapPropertyParams NewProp_LightsAndSwitches;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_MaxCount_MetaData[];
#endif
		static const UECodeGen_Private::FIntPropertyParams NewProp_MaxCount;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_OurVisibleComponent_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_OurVisibleComponent;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_CurrentLightComponent_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_CurrentLightComponent;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_ActorToSpawn_MetaData[];
#endif
		static const UECodeGen_Private::FClassPropertyParams NewProp_ActorToSpawn;
		static const UECodeGen_Private::FPropertyParamsBase* const PropPointers[];
		static const FCppClassTypeInfoStatic StaticCppClassTypeInfo;
		static const UECodeGen_Private::FClassParams ClassParams;
	};
	UObject* (*const Z_Construct_UClass_ABuilding_Statics::DependentSingletons[])() = {
		(UObject* (*)())Z_Construct_UClass_AActor,
		(UObject* (*)())Z_Construct_UPackage__Script_IFC2UE,
	};
	const FClassFunctionLinkInfo Z_Construct_UClass_ABuilding_Statics::FuncInfo[] = {
		{ &Z_Construct_UFunction_ABuilding_AddLightToComponent, "AddLightToComponent" }, // 3355794539
		{ &Z_Construct_UFunction_ABuilding_ComponentClicked, "ComponentClicked" }, // 2516204372
		{ &Z_Construct_UFunction_ABuilding_OnOverlapBegin, "OnOverlapBegin" }, // 2308300354
		{ &Z_Construct_UFunction_ABuilding_OnOverlapEnd, "OnOverlapEnd" }, // 4199300855
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_ABuilding_Statics::Class_MetaDataParams[] = {
		{ "BlueprintType", "true" },
		{ "Comment", "/**\n * \n */" },
		{ "IncludePath", "Building.h" },
		{ "IsBlueprintBase", "true" },
		{ "ModuleRelativePath", "Public/Building.h" },
		{ "ShortTooltip", "Place the this asset in the same folder as your building assets to place the building as a whole." },
	};
#endif
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_ABuilding_Statics::NewProp_PathToMeshes_MetaData[] = {
		{ "Category", "Building" },
		{ "DisplayName", "Path to the building assets" },
		{ "ModuleRelativePath", "Public/Building.h" },
	};
#endif
	const UECodeGen_Private::FStrPropertyParams Z_Construct_UClass_ABuilding_Statics::NewProp_PathToMeshes = { "PathToMeshes", nullptr, (EPropertyFlags)0x0010010000000005, UECodeGen_Private::EPropertyGenFlags::Str, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(ABuilding, PathToMeshes), METADATA_PARAMS(Z_Construct_UClass_ABuilding_Statics::NewProp_PathToMeshes_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_ABuilding_Statics::NewProp_PathToMeshes_MetaData)) };
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches_ValueProp = { "LightsAndSwitches", nullptr, (EPropertyFlags)0x0000000000080008, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, 1, Z_Construct_UClass_UPointLightComponent_NoRegister, METADATA_PARAMS(nullptr, 0) };
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches_Key_KeyProp = { "LightsAndSwitches_Key", nullptr, (EPropertyFlags)0x0000000000080008, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, 0, Z_Construct_UClass_USphereComponent_NoRegister, METADATA_PARAMS(nullptr, 0) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches_MetaData[] = {
		{ "EditInline", "true" },
		{ "ModuleRelativePath", "Public/Building.h" },
	};
#endif
	const UECodeGen_Private::FMapPropertyParams Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches = { "LightsAndSwitches", nullptr, (EPropertyFlags)0x0010008000000008, UECodeGen_Private::EPropertyGenFlags::Map, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(ABuilding, LightsAndSwitches), EMapPropertyFlags::None, METADATA_PARAMS(Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches_MetaData)) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_ABuilding_Statics::NewProp_MaxCount_MetaData[] = {
		{ "Category", "Max" },
		{ "ModuleRelativePath", "Public/Building.h" },
	};
#endif
	const UECodeGen_Private::FIntPropertyParams Z_Construct_UClass_ABuilding_Statics::NewProp_MaxCount = { "MaxCount", nullptr, (EPropertyFlags)0x0010010000000015, UECodeGen_Private::EPropertyGenFlags::Int, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(ABuilding, MaxCount), METADATA_PARAMS(Z_Construct_UClass_ABuilding_Statics::NewProp_MaxCount_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_ABuilding_Statics::NewProp_MaxCount_MetaData)) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_ABuilding_Statics::NewProp_OurVisibleComponent_MetaData[] = {
		{ "Category", "IFC2UE" },
		{ "EditInline", "true" },
		{ "ModuleRelativePath", "Public/Building.h" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UClass_ABuilding_Statics::NewProp_OurVisibleComponent = { "OurVisibleComponent", nullptr, (EPropertyFlags)0x0010000000080009, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(ABuilding, OurVisibleComponent), Z_Construct_UClass_USceneComponent_NoRegister, METADATA_PARAMS(Z_Construct_UClass_ABuilding_Statics::NewProp_OurVisibleComponent_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_ABuilding_Statics::NewProp_OurVisibleComponent_MetaData)) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_ABuilding_Statics::NewProp_CurrentLightComponent_MetaData[] = {
		{ "Category", "IFC2UE" },
		{ "EditInline", "true" },
		{ "ModuleRelativePath", "Public/Building.h" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UClass_ABuilding_Statics::NewProp_CurrentLightComponent = { "CurrentLightComponent", nullptr, (EPropertyFlags)0x0010000000080009, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(ABuilding, CurrentLightComponent), Z_Construct_UClass_UPrimitiveComponent_NoRegister, METADATA_PARAMS(Z_Construct_UClass_ABuilding_Statics::NewProp_CurrentLightComponent_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_ABuilding_Statics::NewProp_CurrentLightComponent_MetaData)) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_ABuilding_Statics::NewProp_ActorToSpawn_MetaData[] = {
		{ "AllowPrivateAccess", "true" },
		{ "Category", "IFC2UE" },
		{ "ModuleRelativePath", "Public/Building.h" },
	};
#endif
	const UECodeGen_Private::FClassPropertyParams Z_Construct_UClass_ABuilding_Statics::NewProp_ActorToSpawn = { "ActorToSpawn", nullptr, (EPropertyFlags)0x0044000000000001, UECodeGen_Private::EPropertyGenFlags::Class, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(ABuilding, ActorToSpawn), Z_Construct_UClass_AActor_NoRegister, Z_Construct_UClass_UClass, METADATA_PARAMS(Z_Construct_UClass_ABuilding_Statics::NewProp_ActorToSpawn_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_ABuilding_Statics::NewProp_ActorToSpawn_MetaData)) };
	const UECodeGen_Private::FPropertyParamsBase* const Z_Construct_UClass_ABuilding_Statics::PropPointers[] = {
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_ABuilding_Statics::NewProp_PathToMeshes,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches_ValueProp,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches_Key_KeyProp,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_ABuilding_Statics::NewProp_LightsAndSwitches,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_ABuilding_Statics::NewProp_MaxCount,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_ABuilding_Statics::NewProp_OurVisibleComponent,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_ABuilding_Statics::NewProp_CurrentLightComponent,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_ABuilding_Statics::NewProp_ActorToSpawn,
	};
	const FCppClassTypeInfoStatic Z_Construct_UClass_ABuilding_Statics::StaticCppClassTypeInfo = {
		TCppClassTypeTraits<ABuilding>::IsAbstract,
	};
	const UECodeGen_Private::FClassParams Z_Construct_UClass_ABuilding_Statics::ClassParams = {
		&ABuilding::StaticClass,
		"Engine",
		&StaticCppClassTypeInfo,
		DependentSingletons,
		FuncInfo,
		Z_Construct_UClass_ABuilding_Statics::PropPointers,
		nullptr,
		UE_ARRAY_COUNT(DependentSingletons),
		UE_ARRAY_COUNT(FuncInfo),
		UE_ARRAY_COUNT(Z_Construct_UClass_ABuilding_Statics::PropPointers),
		0,
		0x009000A4u,
		METADATA_PARAMS(Z_Construct_UClass_ABuilding_Statics::Class_MetaDataParams, UE_ARRAY_COUNT(Z_Construct_UClass_ABuilding_Statics::Class_MetaDataParams))
	};
	UClass* Z_Construct_UClass_ABuilding()
	{
		if (!Z_Registration_Info_UClass_ABuilding.OuterSingleton)
		{
			UECodeGen_Private::ConstructUClass(Z_Registration_Info_UClass_ABuilding.OuterSingleton, Z_Construct_UClass_ABuilding_Statics::ClassParams);
		}
		return Z_Registration_Info_UClass_ABuilding.OuterSingleton;
	}
	template<> IFC2UE_API UClass* StaticClass<ABuilding>()
	{
		return ABuilding::StaticClass();
	}
	DEFINE_VTABLE_PTR_HELPER_CTOR(ABuilding);
	struct Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_Statics
	{
		static const FClassRegisterCompiledInInfo ClassInfo[];
	};
	const FClassRegisterCompiledInInfo Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_Statics::ClassInfo[] = {
		{ Z_Construct_UClass_ABuilding, ABuilding::StaticClass, TEXT("ABuilding"), &Z_Registration_Info_UClass_ABuilding, CONSTRUCT_RELOAD_VERSION_INFO(FClassReloadVersionInfo, sizeof(ABuilding), 4161883260U) },
	};
	static FRegisterCompiledInInfo Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_385086145(TEXT("/Script/IFC2UE"),
		Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_Statics::ClassInfo, UE_ARRAY_COUNT(Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_Building_h_Statics::ClassInfo),
		nullptr, 0,
		nullptr, 0);
PRAGMA_ENABLE_DEPRECATION_WARNINGS
