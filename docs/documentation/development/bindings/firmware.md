---
layout: documentation
---

{% include base.html %}

# Firmware Update

A binding may optionally support the firmware update of its supported devices. For each supported thing type the binding developer must implement the interface `FirmwareUpdateHandler` in the `ThingHandler`. The `FirmwareUpdateHandler` provides two methods: 

- `isUpdateable()` to check, whether the device's firmware can be updated 
- `updateFirmware()` to actually update the firmware

The service `FirmwareUpdateService` tracks each `FirmwareUpdateHandler` and is the main entry point for triggering the firmware update for a given thing. It also provides access to the current `FirmwareStatus` of a specific thing. 

The `FirmwareStatus` could be one of these: 

- *UNKNOWN* - the firmware status cannot be determined
- *CURRENT* - the firmware of the thing is the newest one
- *UPDATEABLE* - the firmware of the thing is outdated and could be updated

If the `FirmwareStatus` changes, a `FirmwareStatusEvent` is created and sent to the topic `smarthome/things/{thingUID}/firmware/status` there `thingUID` is the UID of the thing whose firmware status has been changed. 

The firmware itself is described by the class `Firmware`m that contains the following meta information about a specific firmware: 

- `vendor` - the device vendor 
- `model` - the device model
- `description` - description of this firmware release
- `version` - firmware version
- `prerequisiteVersion` - if specified, the required previous version to update to this firmware version
- `changelog` - the change log of the firmware
- `onlinChangeLog` - an URL to an online version of the change log
- `content` - an `InputStream` to the firmware image

A device manufacturer may register different `FirmwareProvider`s to provide firmware for specific thing types. A `FirmwareProvider` must implement methods to get firmwares for a given thing type and to get a specific firmware identified by its `FirmwareUID`. In addition these methods use a `Locale` argument as the meta data of a firmware is local specific. 

## FileSystemFirmwareProvider

The `FileSystemFirmwareProvider` implements the `FirmwareProvider` interface to register a firmware provider from a file system structure. It retrieves the fimrware images and the firmware metadata from the `{userdir}/firmware` directory. The directory must follow a given folder structure: a sub folder for each binding and within the binding folders a sub folder for each thing type. 

``` 
{userdir}
   +-firmware
          +-{binding-id1}
                  +-{thing-type-id1}
                  +-{thing-type-id2}
          +-{binding-id2}
                  +-{thing-type-id3}
                  +-{thing-type-id4}
``` 

For each firmware an image file must be placed in the according directory and at least one property file containing the following values: 

```  
# the firmware version
version = V1.2-b18
# the name of the associated image file
image = imageV12b18.bin
# (optional) the model name
model = Model A
# (optional) the manufacturer
vendor = Company Ltd.
# (optiona) a description
description = This is a camera.
# (optional) the change log 
changelog = This is the change log
# (optional) an URL of the online change log
onlineChangelog = http://www.company.com
``` 

Additional property files may be placed into this directory with localized values for description, changelog and onlineChangeLog (e.g. *_de.properties). 

## Summary

- A _binding developer_ must implement `FirmwareUpdateHandler` to actually update the firmware of the binding's thing types. 
- A _device manufacturer_ provides the firmware for specific devices with a `FirmwareProvider`. 





