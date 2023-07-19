// Fill out your copyright notice in the Description page of Project Settings.


#include "LightAndSwitch.h"

FLightAndSwitch::FLightAndSwitch() : voltage(0)
{
}

FLightAndSwitch::FLightAndSwitch(FString LightObject, FString LightSwitch, const int Voltage)
{
	this->lightObject = LightObject;
	this->lightSwitch = LightSwitch;
	this->voltage = Voltage;
}

FLightAndSwitch::~FLightAndSwitch()
{
}
