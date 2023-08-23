# Eclipse JDT Language Server (Modified)

This is a modified [Eclipse JDT Language Server](https://github.com/eclipse-jdtls/eclipse.jdt.ls) that only produces suggestions when it is confident that continuing the current valid program with one of the suggestions will result in a valid program.

## What are changed?

Briefly speaking, a new language server request `newCompletion` is implemented that will return a `null` list when it cannot determine if any completion will yield a valid program. The detailed changes are listed [here](https://github.com/UniverseFly/eclipse.jdt.ls/compare/ac420915249aff2de1044c551afd1a7694a8122d...main).

## Installation from source

Clone the repository via `git clone` and build the project via `JAVA_HOME=/path/to/java/11 ./mvnw clean verify -DskipTests=true` to by-pass the tests. This command builds the server into the `./org.eclipse.jdt.ls.product/target/repository` folder.

## Running from the command line

`cd` into `./org.eclipse.jdt.ls.product/target/repository`.

To start the server in the active terminal, adjust the following command as described further below and run it:

```bash
java \
	-Declipse.application=org.eclipse.jdt.ls.core.id1 \
	-Dosgi.bundles.defaultStartLevel=4 \
	-Declipse.product=org.eclipse.jdt.ls.core.product \
	-Dlog.level=ALL \
	-noverify \
	-Xmx1G \
	--add-modules=ALL-SYSTEM \
	--add-opens java.base/java.util=ALL-UNNAMED \
	--add-opens java.base/java.lang=ALL-UNNAMED \
	-jar ./plugins/org.eclipse.equinox.launcher_1.5.200.v20180922-1751.jar \
	-configuration ./config_linux \
	-data /path/to/data
```

1. Choose a value for `-configuration`: this is the path to your platform's configuration directory. For Linux, use `./config_linux`. For windows, use `./config_win`. For mac/OS X, use `./config_mac`.
2. Change the filename of the jar in `-jar ./plugins/...` to match the version you built or downloaded.
3. Choose a value for `-data`: An absolute path to your data directory. eclipse.jdt.ls stores workspace specific information in it. This should be unique per workspace/project.

If you want to debug eclipse.jdt.ls itself, add `-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1044` right after `java` and ensure nothing else is running on port 1044. If you want to debug from the start of execution, change `suspend=n` to `suspend=y` so the JVM will wait for your debugger prior to starting the server.

## License

EPL 2.0, See [LICENSE](LICENSE) file.
