// Copyright Epic Games, Inc. All Rights Reserved.

using UnrealBuildTool;

public class IFC2UE : ModuleRules
{
	public IFC2UE(ReadOnlyTargetRules Target) : base(Target)
	{
		PCHUsage = ModuleRules.PCHUsageMode.UseExplicitOrSharedPCHs;


		PublicIncludePaths.AddRange(
			new string[] {
				// ... add public include paths required here ...
			}
			);


		PrivateIncludePaths.AddRange(
			new string[] {
				"IFC2UE/Private"
			}
			);


		PublicDependencyModuleNames.AddRange(
			new string[]
			{
				"Core",
				"Http",
				"Json",
				"JsonUtilities",
				"UMG",
				"EditorScriptingUtilities",
				"UnrealEd",
				"AssetTools"
				// ... add other public dependencies that you statically link with here ...
			}
			);


		PrivateDependencyModuleNames.AddRange(
			new string[]
			{
				"Projects",
				"InputCore",
				"UnrealEd",
				"ToolMenus",
				"CoreUObject",
				"Engine",
				"Slate",
				"SlateCore",
				"SlateRHIRenderer",
				"DesktopPlatform",
				"Http",
				"EditorScriptingUtilities"
				// ... add private dependencies that you statically link with here ...	
			}
			);


		DynamicallyLoadedModuleNames.AddRange(
			new string[]
			{
				// ... add any modules that your module loads dynamically here ...
			}
			);
	}
}
