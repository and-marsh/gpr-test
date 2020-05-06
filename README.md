Add `local.properties` file with content
```
ndk.dir=/Users/and_marsh/Library/Android/sdk/ndk-bundle
sdk.dir=/Users/and_marsh/Library/Android/sdk
```
and `~/.gradle/gradle.properties` with properties
```
TEST_GITHUB_USERNAME=<username>
TEST_GITHUB_TOKEN=<token>
```
then try
```
gradle assemble publish
```
It causes error during publishing
```
> Task :publishKotlinMultiplatformPublicationToGithubPackageRegistryRepository FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':publishKotlinMultiplatformPublicationToGithubPackageRegistryRepository'.
> Failed to publish publication 'kotlinMultiplatform' to repository 'GithubPackageRegistry'
   > Could not PUT 'https://maven.pkg.github.com/and-marsh/gpr-test/com/example/github_test/0.0.1/github_test-0.0.1.pom'. Received status code 422 from server: Unprocessable Entity
```

Everything is fine for gradle version `5.5.1` in `gpr-test/gradle/wrapper/gradle-wrapper.properties`
