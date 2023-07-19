// Copyright Epic Games, Inc. All Rights Reserved.

#pragma once

#include "CoreMinimal.h"
#include "Framework/Commands/Commands.h"
#include "IFC2UEStyle.h"

class FIFC2UECommands : public TCommands<FIFC2UECommands>
{
public:

	FIFC2UECommands()
		: TCommands<FIFC2UECommands>(TEXT("IFC2UE"), NSLOCTEXT("Contexts", "IFC2UE", "IFC2UE Plugin"), NAME_None, FIFC2UEStyle::GetStyleSetName())
	{
	}

	// TCommands<> interface
	virtual void RegisterCommands() override;

public:
	TSharedPtr< FUICommandInfo > PluginAction;
};
