// Copyright Epic Games, Inc. All Rights Reserved.

#include "IFC2UE.h"

#include "Building.h"
#include "ContentBrowserModule.h"
#include "FileImportHandler.h"
#include "IContentBrowserSingleton.h"
#include "IFC2UEStyle.h"
#include "IFC2UECommands.h"
#include "ToolMenus.h"
#include "UserInterface.h"
#include "AssetRegistry/AssetRegistryModule.h"
#include "Framework/Notifications/NotificationManager.h"
#include "MainFrame/Public/Interfaces/IMainFrameModule.h"
#include "Widgets/Notifications/SNotificationList.h"

class IMainFrameModule;
static const FName IFC2UETabName("IFC2UE");

#define LOCTEXT_NAMESPACE "FIFC2UEModule"

void FIFC2UEModule::StartupModule()
{
	// This code will execute after your module is loaded into memory; the exact timing is specified in the .uplugin file per-module

	BuildingActions = MakeShared<FBuildingActions>();
	FAssetToolsModule::GetModule().Get().RegisterAssetTypeActions(BuildingActions.ToSharedRef());

	FIFC2UEStyle::Initialize();
	FIFC2UEStyle::ReloadTextures();

	FIFC2UECommands::Register();
	
	PluginCommands = MakeShareable(new FUICommandList);

	PluginCommands->MapAction(
		FIFC2UECommands::Get().PluginAction,
		FExecuteAction::CreateRaw(this, &FIFC2UEModule::PluginButtonClicked),
		FCanExecuteAction());

	UToolMenus::RegisterStartupCallback(FSimpleMulticastDelegate::FDelegate::CreateRaw(this, &FIFC2UEModule::RegisterMenus));
	FGlobalTabmanager::Get()->RegisterNomadTabSpawner(IFC2UETabName,
		FOnSpawnTab::CreateRaw(this, &FIFC2UEModule::OnSpawnPluginTab))
		.SetDisplayName(LOCTEXT("FIFC2UETabTitle", "IFC Importer"))
		.SetMenuType(ETabSpawnerMenuType::Hidden);
}

void FIFC2UEModule::ShutdownModule()
{
	// This function may be called during shutdown to clean up your module.  For modules that support dynamic reloading,
	// we call this function before unloading the module.

	if (!FModuleManager::Get().IsModuleLoaded("AssetTools")) return;
	FAssetToolsModule::GetModule().Get().UnregisterAssetTypeActions(BuildingActions.ToSharedRef());

	UToolMenus::UnRegisterStartupCallback(this);

	UToolMenus::UnregisterOwner(this);

	FIFC2UEStyle::Shutdown();

	FIFC2UECommands::Unregister();
}


void FIFC2UEModule::PluginButtonClicked()
{
	FGlobalTabmanager::Get()->TryInvokeTab(IFC2UETabName);
}

void FIFC2UEModule::RegisterMenus()
{
	// Owner will be used for cleanup in call to UToolMenus::UnregisterOwner
	FToolMenuOwnerScoped OwnerScoped(this);

	{
		UToolMenu* Menu = UToolMenus::Get()->ExtendMenu("LevelEditor.MainMenu.Window");
		{
			FToolMenuSection& Section = Menu->FindOrAddSection("WindowLayout");
			Section.AddMenuEntryWithCommandList(FIFC2UECommands::Get().PluginAction, PluginCommands);
		}
	}

	{
		UToolMenu* ToolbarMenu = UToolMenus::Get()->ExtendMenu("LevelEditor.LevelEditorToolBar.PlayToolBar");
		{
			FToolMenuSection& Section = ToolbarMenu->FindOrAddSection("PluginTools");
			{
				FToolMenuEntry& Entry = Section.AddEntry(FToolMenuEntry::InitToolBarButton(FIFC2UECommands::Get().PluginAction));
				Entry.SetCommandList(PluginCommands);
			}
		}
	}
}

TSharedRef<SDockTab> FIFC2UEModule::OnSpawnPluginTab(const FSpawnTabArgs& SpawnTabArgs)
{
	FileImportHandler* importHandler = new FileImportHandler();
	return importHandler->ShowImportWindow();
}


#undef LOCTEXT_NAMESPACE
	
IMPLEMENT_MODULE(FIFC2UEModule, IFC2UE)