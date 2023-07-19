// Copyright Epic Games, Inc. All Rights Reserved.

#include "IFC2UECommands.h"

#define LOCTEXT_NAMESPACE "FIFC2UEModule"

void FIFC2UECommands::RegisterCommands()
{
	UI_COMMAND(PluginAction, "IFC2UE", "Execute IFC2UE action", EUserInterfaceActionType::Button, FInputChord());
}

#undef LOCTEXT_NAMESPACE
