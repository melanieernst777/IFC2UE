// Copyright Epic Games, Inc. All Rights Reserved.
/*===========================================================================
	Generated code exported from UnrealHeaderTool.
	DO NOT modify this manually! Edit the corresponding .h files instead!
===========================================================================*/

#include "UObject/GeneratedCppIncludes.h"
#include "IFC2UE/Public/BuildingAssetData.h"
PRAGMA_DISABLE_DEPRECATION_WARNINGS
void EmptyLinkFunctionForGeneratedCodeBuildingAssetData() {}
// Cross Module References
	IFC2UE_API UClass* Z_Construct_UClass_UBuildingAssetData_NoRegister();
	IFC2UE_API UClass* Z_Construct_UClass_UBuildingAssetData();
	ENGINE_API UClass* Z_Construct_UClass_UAssetUserData();
	UPackage* Z_Construct_UPackage__Script_IFC2UE();
	ENGINE_API UClass* Z_Construct_UClass_UStaticMesh_NoRegister();
// End Cross Module References
	void UBuildingAssetData::StaticRegisterNativesUBuildingAssetData()
	{
	}
	IMPLEMENT_CLASS_NO_AUTO_REGISTRATION(UBuildingAssetData);
	UClass* Z_Construct_UClass_UBuildingAssetData_NoRegister()
	{
		return UBuildingAssetData::StaticClass();
	}
	struct Z_Construct_UClass_UBuildingAssetData_Statics
	{
		static UObject* (*const DependentSingletons[])();
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam Class_MetaDataParams[];
#endif
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_LightSwitch_MetaData[];
#endif
		static const UECodeGen_Private::FObjectPropertyParams NewProp_LightSwitch;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_SomeString_MetaData[];
#endif
		static const UECodeGen_Private::FStrPropertyParams NewProp_SomeString;
#if WITH_METADATA
		static const UECodeGen_Private::FMetaDataPairParam NewProp_IsLight_MetaData[];
#endif
		static void NewProp_IsLight_SetBit(void* Obj);
		static const UECodeGen_Private::FBoolPropertyParams NewProp_IsLight;
		static const UECodeGen_Private::FPropertyParamsBase* const PropPointers[];
		static const FCppClassTypeInfoStatic StaticCppClassTypeInfo;
		static const UECodeGen_Private::FClassParams ClassParams;
	};
	UObject* (*const Z_Construct_UClass_UBuildingAssetData_Statics::DependentSingletons[])() = {
		(UObject* (*)())Z_Construct_UClass_UAssetUserData,
		(UObject* (*)())Z_Construct_UPackage__Script_IFC2UE,
	};
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_UBuildingAssetData_Statics::Class_MetaDataParams[] = {
		{ "Comment", "/**\n * \n */" },
		{ "IncludePath", "BuildingAssetData.h" },
		{ "ModuleRelativePath", "Public/BuildingAssetData.h" },
	};
#endif
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_LightSwitch_MetaData[] = {
		{ "Category", "Custom Data" },
		{ "ModuleRelativePath", "Public/BuildingAssetData.h" },
	};
#endif
	const UECodeGen_Private::FObjectPropertyParams Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_LightSwitch = { "LightSwitch", nullptr, (EPropertyFlags)0x0010010000000005, UECodeGen_Private::EPropertyGenFlags::Object, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(UBuildingAssetData, LightSwitch), Z_Construct_UClass_UStaticMesh_NoRegister, METADATA_PARAMS(Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_LightSwitch_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_LightSwitch_MetaData)) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_SomeString_MetaData[] = {
		{ "Category", "Custom Data" },
		{ "ModuleRelativePath", "Public/BuildingAssetData.h" },
	};
#endif
	const UECodeGen_Private::FStrPropertyParams Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_SomeString = { "SomeString", nullptr, (EPropertyFlags)0x0010010000000005, UECodeGen_Private::EPropertyGenFlags::Str, RF_Public|RF_Transient|RF_MarkAsNative, 1, STRUCT_OFFSET(UBuildingAssetData, SomeString), METADATA_PARAMS(Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_SomeString_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_SomeString_MetaData)) };
#if WITH_METADATA
	const UECodeGen_Private::FMetaDataPairParam Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_IsLight_MetaData[] = {
		{ "Category", "Custom Data" },
		{ "ModuleRelativePath", "Public/BuildingAssetData.h" },
	};
#endif
	void Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_IsLight_SetBit(void* Obj)
	{
		((UBuildingAssetData*)Obj)->IsLight = 1;
	}
	const UECodeGen_Private::FBoolPropertyParams Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_IsLight = { "IsLight", nullptr, (EPropertyFlags)0x0010010000000005, UECodeGen_Private::EPropertyGenFlags::Bool | UECodeGen_Private::EPropertyGenFlags::NativeBool, RF_Public|RF_Transient|RF_MarkAsNative, 1, sizeof(bool), sizeof(UBuildingAssetData), &Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_IsLight_SetBit, METADATA_PARAMS(Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_IsLight_MetaData, UE_ARRAY_COUNT(Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_IsLight_MetaData)) };
	const UECodeGen_Private::FPropertyParamsBase* const Z_Construct_UClass_UBuildingAssetData_Statics::PropPointers[] = {
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_LightSwitch,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_SomeString,
		(const UECodeGen_Private::FPropertyParamsBase*)&Z_Construct_UClass_UBuildingAssetData_Statics::NewProp_IsLight,
	};
	const FCppClassTypeInfoStatic Z_Construct_UClass_UBuildingAssetData_Statics::StaticCppClassTypeInfo = {
		TCppClassTypeTraits<UBuildingAssetData>::IsAbstract,
	};
	const UECodeGen_Private::FClassParams Z_Construct_UClass_UBuildingAssetData_Statics::ClassParams = {
		&UBuildingAssetData::StaticClass,
		nullptr,
		&StaticCppClassTypeInfo,
		DependentSingletons,
		nullptr,
		Z_Construct_UClass_UBuildingAssetData_Statics::PropPointers,
		nullptr,
		UE_ARRAY_COUNT(DependentSingletons),
		0,
		UE_ARRAY_COUNT(Z_Construct_UClass_UBuildingAssetData_Statics::PropPointers),
		0,
		0x003010A0u,
		METADATA_PARAMS(Z_Construct_UClass_UBuildingAssetData_Statics::Class_MetaDataParams, UE_ARRAY_COUNT(Z_Construct_UClass_UBuildingAssetData_Statics::Class_MetaDataParams))
	};
	UClass* Z_Construct_UClass_UBuildingAssetData()
	{
		if (!Z_Registration_Info_UClass_UBuildingAssetData.OuterSingleton)
		{
			UECodeGen_Private::ConstructUClass(Z_Registration_Info_UClass_UBuildingAssetData.OuterSingleton, Z_Construct_UClass_UBuildingAssetData_Statics::ClassParams);
		}
		return Z_Registration_Info_UClass_UBuildingAssetData.OuterSingleton;
	}
	template<> IFC2UE_API UClass* StaticClass<UBuildingAssetData>()
	{
		return UBuildingAssetData::StaticClass();
	}
	DEFINE_VTABLE_PTR_HELPER_CTOR(UBuildingAssetData);
	struct Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_BuildingAssetData_h_Statics
	{
		static const FClassRegisterCompiledInInfo ClassInfo[];
	};
	const FClassRegisterCompiledInInfo Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_BuildingAssetData_h_Statics::ClassInfo[] = {
		{ Z_Construct_UClass_UBuildingAssetData, UBuildingAssetData::StaticClass, TEXT("UBuildingAssetData"), &Z_Registration_Info_UClass_UBuildingAssetData, CONSTRUCT_RELOAD_VERSION_INFO(FClassReloadVersionInfo, sizeof(UBuildingAssetData), 571808095U) },
	};
	static FRegisterCompiledInInfo Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_BuildingAssetData_h_4214309346(TEXT("/Script/IFC2UE"),
		Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_BuildingAssetData_h_Statics::ClassInfo, UE_ARRAY_COUNT(Z_CompiledInDeferFile_FID_HostProject_Plugins_IFC2UE_Source_IFC2UE_Public_BuildingAssetData_h_Statics::ClassInfo),
		nullptr, 0,
		nullptr, 0);
PRAGMA_ENABLE_DEPRECATION_WARNINGS
