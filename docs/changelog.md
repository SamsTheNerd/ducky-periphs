# Changelog

## 1.2.3

### Fixed

- Unchecked keyboard packets
- Focal Link not dropping in survival
- Hexal entity anchored gates crashing

## 1.2.2

### Fixed

- Forge moreIotas/Hexal weirdness

### Added

- Support for hexal 0.2.16 and typed gates.
- Focal Link
- Ability to click on monitors while using a keyboard

### Changed

- Can now read in truenames to cc, but still can't write them

## 1.2.1

### Fixed

- Removed debug log spam

## 1.2.0

### Added

- Forge !
- Peripherals for akashic records, akashic bookshelves, slates, and impetuses
- getIotaType method for focal port

### Changed

- Now requires architectury api
- Mote iota representation to prevent arbitrary access
- Null iota representation to work better with lists

### Fixed

- Bugs related to empty focus in focal port (#12, #16, #13?)
- Bug with non-character keys not sending from keyboard (#10)
- Bug with blocks not being pickaxe mineable without hexcasting installed (#9)
- List with null behaving improperly (#17)

## 1.1.5

### Added

- Ducky Banner Pattern
- Conjurable ducky hexcasting spell.
- Precustomized keyboards as dungeon loot
- Hexal Mote Support

### Changed

- Focal ports reworked to allow swapping focus in and out.

### Fixed

- Sodium biome blending the ducks
- Focal port not behaving without hexal/moreiotas

## 1.1.4

### Fixed

- Crash when using cardinal components without hex casting.

## 1.1.3

### Fixed

- Focal Port Writing Lists
- Gem demoted ducks from omnipotent 6th dimensional entity status (made quack mono and quieter)

### Added

- moreIotas string and matrix support

## 1.1.2

### Fixed
- Prevent focal port entity from being teleported or moved

### Changed
- Added peripheral name to event return values as the 2nd return value. Everything else moves down as a result. Check wiki for more details.

## 1.1.1

### Added
- Focal Port Peripheral to interface between CC and hex casting
- Hexal Support

### Fixed
- Color provider reregistered (it got removed in an earlier code rework)

## 1.1.0

### Added
- MC Version 1.19.2 support
- Sculkohpone peripheral

### Fixed
- Ducks not quacking properly with redstone
- Keyboard sending double presses

## 1.0.2

### Added
- Glow effect on entity detector through continuity, iris, and shaderpacks
- Proper keyboard item models
- Icon and updated description for the fabric.mod.json

### Changes
- Entity Detector now gives relative coordinates
- Entity Detector events are renamed to match CC standard

### Fixed
- Crash when teleporting away from weather machine
- REI opening up with the keyboard

<br>

## Start of changelog