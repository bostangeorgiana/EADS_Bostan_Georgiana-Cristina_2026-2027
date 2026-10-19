package com.example.lab2.advanced.components;

import com.example.lab2.advanced.MyComponent;
import com.example.lab2.advanced.external.ExternalLib;

// clasa lipsta -> nu se poate incarca in JVM
@MyComponent
public class BrokenComponent extends ExternalLib {
}
