#  Advancement Macros

Advancement Macros is a Fabric mod for Minecraft 1.20.2+ that passes the data of criterion triggers onto the reward function of an advancement using function macros, providing more flexibility for datapack users. [Check the wiki](https://github.com/eggohito/advancement-macros/wiki) for more information.
<br>


##  Using as a dependency

Advancement Macros can be used as a dependency to provide compatibility to one's mod that may provide a custom criterion trigger. First, you'll need to setup your development environment by:
<br>
<br>

1. Add JitPack as a repository in the `repositories` block of your `build.gradle` file:
```groove
repositories {
    maven {
        name = "JitPack"
        url = "https://jitpack.io"
    }
}
```
<br>

2. Add the mod as a dependency in the `dependencies` block of your `build.gradle` file, where `<version>` is the version you desire to use.
[![](https://jitpack.io/v/eggohito/advancement-macros.svg)](https://jitpack.io/#eggohito/advancement-macros)
```groove
dependencies {
    modImplementation "com.github.eggohito:advancement-macros:<version>"
}
```
