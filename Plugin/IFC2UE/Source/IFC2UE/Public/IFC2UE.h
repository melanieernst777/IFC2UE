// Copyright Epic Games, Inc. All Rights Reserved.

#pragma once

#include "CoreMinimal.h"
#include <IFC2UE/Public/BuildingActions.h>

class FHttpModule;
class FToolBarBuilder;
class FMenuBuilder;

class FIFC2UEModule : public IModuleInterface
{
public:

	/** IModuleInterface implementation */
	virtual void StartupModule() override;
	virtual void ShutdownModule() override;
	void PluginButtonClicked();
	
private:
	void RegisterMenus();
	TSharedPtr<class FUICommandList> PluginCommands;
	TSharedPtr<FBuildingActions> BuildingActions;
	TSharedRef<class SDockTab> OnSpawnPluginTab(const class FSpawnTabArgs& SpawnTabArgs);
};

