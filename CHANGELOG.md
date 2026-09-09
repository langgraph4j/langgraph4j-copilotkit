# Changelog



<!-- "name: v0.2.0-beta1" is a release tag -->

## [v0.2.0-beta1](https://github.com/bsorrentino/langgraph4j/releases/tag/v0.2.0-beta1) (2026-09-09)

### Features

 *  add error handling and conversion to RunErrorEvent in graph execution ([69f3758949395b3](https://github.com/bsorrentino/langgraph4j/commit/69f3758949395b31ee1380fa7dc2e0125d34458f))
   
 *  add AGUIConfigurations class for custom ObjectMapper configuration ([23c7f0dbadcd0ca](https://github.com/bsorrentino/langgraph4j/commit/23c7f0dbadcd0cac12e853441094cdce27064c7a))
   

### Bug Fixes

 -  wrap throwable in Exception for checkpoint release on error ([c31de6a3ccebe51](https://github.com/bsorrentino/langgraph4j/commit/c31de6a3ccebe51f1ace68a2e653424381e80480))

 -  reset ag-ui submodule reference ([262ad87b21ca6b9](https://github.com/bsorrentino/langgraph4j/commit/262ad87b21ca6b9cb9ec65d1d78da091fa3aa9b2))

 -  **AGUIAbstractLangGraphAgent**  correct approval property key in GraphInput.resume method ([19ebeb6f9fdb064](https://github.com/bsorrentino/langgraph4j/commit/19ebeb6f9fdb06414ca067068ac793074e07aefb))


### Documentation

 -  update tech stack versions in README.md ([3be4e8c26d00473](https://github.com/bsorrentino/langgraph4j/commit/3be4e8c26d00473e6f5da7d409eb1aca8b65d0e2))

 -  update changelog ([6319ec169f06782](https://github.com/bsorrentino/langgraph4j/commit/6319ec169f06782918c88c53b86ca47ffbd7ee97))


### Refactor

 -  align implementation to ag-ui community java version 0.1.0 ([60803e1ab69aa6f](https://github.com/bsorrentino/langgraph4j/commit/60803e1ab69aa6f8823be05da9a640776d71a6c8))
   
 -  rename methods for clarity and enhance error handling in event streaming ([72bc8d288f20329](https://github.com/bsorrentino/langgraph4j/commit/72bc8d288f2032921c227d4b9aa420a41e433725))
   
 -  remove Sonatype central publishing plugin and repository configuration from pom.xml ([2dd2d4f31a942ad](https://github.com/bsorrentino/langgraph4j/commit/2dd2d4f31a942ad942a7a67cfed84237585396fa))
   
 -  reorganize project structure ([358f4ab25eb55ac](https://github.com/bsorrentino/langgraph4j/commit/358f4ab25eb55ac1047c5c32df8cd236fda528f9))
    > - update dependency management
 > - add module langgraph4j-ag-ui-client
 > - add module langgraph4j-ag-ui-json

 -  **javelit**  enhance error handling and event logging in JtAGUIClientApp ([3c8e73fb9ecd1bc](https://github.com/bsorrentino/langgraph4j/commit/3c8e73fb9ecd1bce56c31941dd029a8fd283884a))
   
 -  update SSE endpoint to use MediaType.TEXT_EVENT_STREAM and improve event handling ([9f4675acc8f7444](https://github.com/bsorrentino/langgraph4j/commit/9f4675acc8f744457eaa24b021f9459c2ad0fb8f))
   
 -  update buildGraphInput method to include resume parameter and clean up nodeOutputToEvents method ([f5c88f6766e1d39](https://github.com/bsorrentino/langgraph4j/commit/f5c88f6766e1d395be0c1337674cef857104fa15))
   
 -  remove unnecessary Maven version check ([7474bee85996c3c](https://github.com/bsorrentino/langgraph4j/commit/7474bee85996c3ca6d15cafc507c80f4fb1f4535))
   
 -  remove unused module ([1afb95a07a58427](https://github.com/bsorrentino/langgraph4j/commit/1afb95a07a584273ed33bc6014211c106ada9607))
   
 -  **AGUISSEController**  update streamDataWithFlux method to accept raw JSON string ([4e94bbb7a6e6e4f](https://github.com/bsorrentino/langgraph4j/commit/4e94bbb7a6e6e4f9ba57d2d299c8579d8ef1fe31))
   

### ALM 

 -  bump to next version 0.2.0-beta1 ([1d9f54e52c4bd0b](https://github.com/bsorrentino/langgraph4j/commit/1d9f54e52c4bd0bc35707aa23c4c99d0247a6efc))
   
 -  update version to 0.2.0-SNAPSHOT ([a091cdc4742cfde](https://github.com/bsorrentino/langgraph4j/commit/a091cdc4742cfde58472f6b080b8b9197260e9bb))
   
 -  re-add ag-ui submodule ([b7063b8365b166f](https://github.com/bsorrentino/langgraph4j/commit/b7063b8365b166fe487097fe288ba1763aa2ae20))
   
 -  add SCM and issue management sections to pom.xml ([5ad0a89a8dfcd3c](https://github.com/bsorrentino/langgraph4j/commit/5ad0a89a8dfcd3c9c82f181ebca267ffb38afad4))
   
 -  update langgraph4j-javelit version to 1.9.0-beta6 in JtAGUIClientApp.java ([ab68326ef1b7fd0](https://github.com/bsorrentino/langgraph4j/commit/ab68326ef1b7fd016f655af569eb487d39205315))
   
 -  bump to next version 0.1.0 ([424303aea867831](https://github.com/bsorrentino/langgraph4j/commit/424303aea8678318fe2ca2be99ed8719deadeee8))
   
 -  update langgraph4j-bom version to 1.9.0-beta6 in pom.xml ([e404c4c29976edd](https://github.com/bsorrentino/langgraph4j/commit/e404c4c29976edd836d7d26fd5bf06d5788b70bf))
   
 -  update Maven command to include langgraph4j-ag-ui-client in deployment ([021090342d96a33](https://github.com/bsorrentino/langgraph4j/commit/021090342d96a335c5ce80f3dd02d60ce85ae239))
   
 -  add maven-shade-plugin configuration for creating a fat JAR ([52fe4fb88a17def](https://github.com/bsorrentino/langgraph4j/commit/52fe4fb88a17deff68d8bf0ab2cb901949018c56))
   
 -  add Sonatype central publishing plugin and repository configuration ([cfa43aa554e6aac](https://github.com/bsorrentino/langgraph4j/commit/cfa43aa554e6aacaa0de58877ec490e3956555a9))
   
 -  add Spring Framework BOM and update dependencies in pom.xml ([563012df3c27872](https://github.com/bsorrentino/langgraph4j/commit/563012df3c2787218e8a1c60bcb41234b782d78d))
   
 -  reset and re-add ag-ui submodule ([107af73de41d692](https://github.com/bsorrentino/langgraph4j/commit/107af73de41d69233e2baa8642f5b66619e6a0ea))
   
 -  re-add ag-ui submodule ([bcf443658e43d50](https://github.com/bsorrentino/langgraph4j/commit/bcf443658e43d5009c4c1a9351900cd5cd1e48d4))
   
 -  remove broken ag-ui submodule ([0fc34b59a508539](https://github.com/bsorrentino/langgraph4j/commit/0fc34b59a508539062afab4bbd50176a7b213a17))
   
 -  **deploy-snapshot**  comment out push trigger for develop branch ([542caa8b93165db](https://github.com/bsorrentino/langgraph4j/commit/542caa8b93165db57861c8a7647d63d7e507b6c6))
   
 -  Update ag-ui submodule reference ([b6dfbf0971de09b](https://github.com/bsorrentino/langgraph4j/commit/b6dfbf0971de09b5d493b1c3abfaa82aa7182140))
   
 -  remove com.ag-ui.community.java-json dep, add jackson-databind dependency version 2.21.1 ([db76526ebf4e4f1](https://github.com/bsorrentino/langgraph4j/commit/db76526ebf4e4f1a81716ad6df503bc05f84e5e0))
   
 -  **deploy-snapshot**  enable recursive submodule checkout and set fetch depth ([32f731729025a42](https://github.com/bsorrentino/langgraph4j/commit/32f731729025a422acd8dc47d1f55e21a68f47a1))
   
 -  Update ag-ui submodule reference ([f3c4c14185866ec](https://github.com/bsorrentino/langgraph4j/commit/f3c4c14185866ec5a87f7e95a5a7bf15e27eee09))
   
 -  update ag-ui sub module ([2dd3b46122dba0f](https://github.com/bsorrentino/langgraph4j/commit/2dd3b46122dba0f41c082c544d073e8ce4d647ca))
   
 -  **langgraph4j-ag-ui-sdk**  embed com.ag-ui.community.java-json module ([b01faf4c7629c03](https://github.com/bsorrentino/langgraph4j/commit/b01faf4c7629c031073c82d86910bb793eec4649))
   
 -  **deploy-snapshot**  enable submodule checkout in deploy workflow ([421837cb2d4fda9](https://github.com/bsorrentino/langgraph4j/commit/421837cb2d4fda91cb580235f7d170674f352508))
   
 -  **deploy-snapshot**  specify module for version check in SNAPSHOT detection ([0c977d7be4cc191](https://github.com/bsorrentino/langgraph4j/commit/0c977d7be4cc191bc22bd72d0e5aa01b3fe3c1e5))
   
 -  **deploy-snapshot**  set working directory for SNAPSHOT version check ([58c7779d36a442e](https://github.com/bsorrentino/langgraph4j/commit/58c7779d36a442e8a00fa7536de15c71960c9dfe))
   
 -  **deploy-snapshot**  update JDK version from 17 to 21 ([0fa992de5d5e7f9](https://github.com/bsorrentino/langgraph4j/commit/0fa992de5d5e7f9ff9eba0c593defeff53ec551e))
   
 -  **deploy-snapshot**  update Maven command to include clean install step ([56b798ee2d363fd](https://github.com/bsorrentino/langgraph4j/commit/56b798ee2d363fd6ccf583931e9d93846ec23298))
   
 -  **deploy-snapshot**  downgrade setup-java action to version 5 ([2882edee6e86830](https://github.com/bsorrentino/langgraph4j/commit/2882edee6e868300bc06fe5dfe1c027c0238e977))
   
 -  **deploy-snapshot**  update GitHub actions to use latest versions of checkout and setup-java ([ac6ce58b6e80e2d](https://github.com/bsorrentino/langgraph4j/commit/ac6ce58b6e80e2d7ac57568253df6f594f7e52aa))
   
 -  **langgraph4j-ag-ui-sdk**  add maven-shade-plugin for creating fat JARs ([1a436524dc4d91d](https://github.com/bsorrentino/langgraph4j/commit/1a436524dc4d91d531cef72a74f317e1dad1d55d))
   
 -  update ag-ui sdk submodule ([6d59912fda13336](https://github.com/bsorrentino/langgraph4j/commit/6d59912fda13336f33b4a009f0c735e87fd77152))
   
 -  **langgraph4j-ag-ui-sdk**  update ag-ui sdk dependency management ([721a6fb32d806b5](https://github.com/bsorrentino/langgraph4j/commit/721a6fb32d806b5ba599bd5cc0cb5f0298080394))
    > Align the module parent and LangGraph4j BOM with 1.9-SNAPSHOT. Upgrade Spring AI and Spring Boot BOM versions, centralize AG-UI dependency versions, and add required Jackson annotations support. Adjust test dependencies for Spring AI BOM-managed versions and add AG-UI OkHttp test support.

 -  update version to 1.9-SNAPSHOT and set Maven compiler release to 21 ([efadcfc9432baba](https://github.com/bsorrentino/langgraph4j/commit/efadcfc9432baba2b46051a20882ae75e566fe60))
   

### Test 

 -  update application.yml add specific port ([29d8f50fcd4e4da](https://github.com/bsorrentino/langgraph4j/commit/29d8f50fcd4e4dafe236155dbc89a6b74d85a892))
   
 -  update buildGraphInput method to include resume parameter for improved state handling ([b7edc0afaccbf87](https://github.com/bsorrentino/langgraph4j/commit/b7edc0afaccbf87faa753213b5cc39ea90143a1f))
   
 -  **deploy-snapshot**  improve SNAPSHOT version check by adding Maven version output ([8ea15f1e52b60c5](https://github.com/bsorrentino/langgraph4j/commit/8ea15f1e52b60c5e1fb5a70b5a99299459133a8e))
   
 -  **langgraph4j-ag-ui-sdk**  refine test application ([bc5ad2a4794c352](https://github.com/bsorrentino/langgraph4j/commit/bc5ad2a4794c352248d495400133d8b7cf10d790))
   
 -  **javelit**  add JtAGUIClientApp class for LangGraph4J AG UI client implementation ([f431de22b5ea980](https://github.com/bsorrentino/langgraph4j/commit/f431de22b5ea980047c28b176d6e1e7f9cc50f04))
   

### Continuous Integration

 -  simplify deployment command in deploy-snapshot.yaml ([9cdedcfa8939001](https://github.com/bsorrentino/langgraph4j/commit/9cdedcfa89390018f8784a4dba4d63dac8988769))
   




<!-- "name: v1.9-20260706" is a release tag -->

## [v1.9-20260706](https://github.com/bsorrentino/langgraph4j/releases/tag/v1.9-20260706) (2026-07-06)











<!-- "name: v0.0.3" is a release tag -->

## [v0.0.3](https://github.com/bsorrentino/langgraph4j/releases/tag/v0.0.3) (2026-03-11)



### Documentation

 -  update readme ([448aba99cec2be0](https://github.com/bsorrentino/langgraph4j/commit/448aba99cec2be009732841de4d77faa30cd8c59))

 -  update readme ([608fa74dbc9688a](https://github.com/bsorrentino/langgraph4j/commit/608fa74dbc9688a2c195d9ce4849be3616348317))

 -  update readme ([3ec5a24f7a6f56c](https://github.com/bsorrentino/langgraph4j/commit/3ec5a24f7a6f56ced5c92851316cc8e38e83353e))

 -  update changelog ([0ec43efbc0f55ae](https://github.com/bsorrentino/langgraph4j/commit/0ec43efbc0f55ae3b7dd45091888337780e170a4))


### Refactor

 -  **AGUIAbstractLangGraphAgent**  replace deprecated isEnd() to isStreamingEnd() in streaming check ([7bcf6e78fd7f375](https://github.com/bsorrentino/langgraph4j/commit/7bcf6e78fd7f375002aa360e0f2df6a3dc1a7c35))
   
 -  **AGUIAbstractLangGraphAgent**  align if/else blocks and enhance streaming output logging ([859d84dc976dc35](https://github.com/bsorrentino/langgraph4j/commit/859d84dc976dc35c8c232eda96f53fde58f5b823))
   

### ALM 

 -  bump to next version 0.0.3 ([38adf9171566ef7](https://github.com/bsorrentino/langgraph4j/commit/38adf9171566ef76b347aaa2446fe7d569b25efe))
   
 -  update lg4j version to 1.8.8 ([a4b5f00527cf91a](https://github.com/bsorrentino/langgraph4j/commit/a4b5f00527cf91adf4c433a7d2fb063628b66a17))
   






<!-- "name: v0.0.2" is a release tag -->

## [v0.0.2](https://github.com/bsorrentino/langgraph4j/releases/tag/v0.0.2) (2026-03-08)

### Features

 *  add new module 'langgraph4j-ag-ui-sdk' with ag-ui sdk integration ([afe040bd8c4e7f1](https://github.com/bsorrentino/langgraph4j/commit/afe040bd8c4e7f104a750ce2a4eeff94581f5a35))
     > add the modern copilotkit app for testing integration
     > resolve #3
   


### Documentation

 -  update project readme ([1f8322bf1dad550](https://github.com/bsorrentino/langgraph4j/commit/1f8322bf1dad550309c62f4c776bf8a6565e3a5f))

 -  **langgraph4j-ag-ui-sdk**  add README ([8f85002ad46e976](https://github.com/bsorrentino/langgraph4j/commit/8f85002ad46e976b9ebb1129e5e81f7d34aac195))

 -  update architectural diagram ([2d6bfd7e5b798a4](https://github.com/bsorrentino/langgraph4j/commit/2d6bfd7e5b798a494a0a6a9656b9b10faafc7505))

 -  update readme ([a8327c539af469b](https://github.com/bsorrentino/langgraph4j/commit/a8327c539af469bbf55c7f9cf40258d97733735d))

 -  ad changelog ([aa109841eb8498b](https://github.com/bsorrentino/langgraph4j/commit/aa109841eb8498bf56a2a8440a98f4b92396d287))


### Refactor

 -  **copilot-app/tsconfig.json**  update jsx setting to preserve ([98edea8ed1533ba](https://github.com/bsorrentino/langgraph4j/commit/98edea8ed1533ba66d1c29cf82f23fa5b6717cf2))
   
 -  **AGUIAbstractLangGraphAgent.java**  Improve streaming event handling by restructuring conditional logic and adding explicit stream start/end markers. ([4e275c633a88cdb](https://github.com/bsorrentino/langgraph4j/commit/4e275c633a88cdb47cee53f327e1aea1a0cabc66))
   
 -  **pom**  Rename parent artifact and update module structure ([7820f56d28bc89b](https://github.com/bsorrentino/langgraph4j/commit/7820f56d28bc89bcc120783f63eb4115a6135923))
    > worn on #3

 -  move previous implementation into a separate module 'langgraph4j-ag-ui-impl' ([046d9ac37e013a5](https://github.com/bsorrentino/langgraph4j/commit/046d9ac37e013a559772b60a9fb7840a3afa89cc))
   
 -  move previous implementation into a separate module 'langgraph4j-ag-ui-impl' ([adb652543342012](https://github.com/bsorrentino/langgraph4j/commit/adb6525433420126cd684c03171ecff12a2eaf80))
   
 -  **AGUILangGraphAgent**  Implement LG4JLoggable interface ([c5bf95d81798a2c](https://github.com/bsorrentino/langgraph4j/commit/c5bf95d81798a2c046c6dff340bc4afd31b9e3b9))
    > Added LG4JLoggable interface implementation to the AGUILangGraphAgent class

 -  **AGUILangGraphAgent**  Refactored method handling node output to events ([e93222f606b3787](https://github.com/bsorrentino/langgraph4j/commit/e93222f606b37872e48dd28af3eac6490c30e292))
    > Extracted method &#x60;nodeOutputToEvents&#x60; from &#x60;nodeOutputToText&#x60; and updated &#x60;onEvent&#x60; logic to use the new method.


### ALM 

 -  bump to next version 0.0.2 ([3df8ccb0411b5f2](https://github.com/bsorrentino/langgraph4j/commit/3df8ccb0411b5f29076d4f5e7718df40c94cd2d2))
   
 -  Update version to 0.0.2-SNAPSHOT and remove langgraph4j-ag-ui-impl module from modules list ([ae80250c90fb560](https://github.com/bsorrentino/langgraph4j/commit/ae80250c90fb5601b0ff336a92953dd847b7d96e))
   
 -  **langgraph4j-ag-ui-sdk/pom.xml**  Update parent version and langgraph4j to 1.8.7 ([8e52853fda9bda9](https://github.com/bsorrentino/langgraph4j/commit/8e52853fda9bda94d8ac6ec2032bd8a70b9187f8))
   
 -  update ag-ui submodule ([02710cd332678f6](https://github.com/bsorrentino/langgraph4j/commit/02710cd332678f6540c2de41af0a006fd62239fd))
   
 -  **copilot-app/package.json**  Update dependencies and package manager to new versions ([004c46c6a51f436](https://github.com/bsorrentino/langgraph4j/commit/004c46c6a51f436df181238e6f4bc71a46f661bd))
    > Updated @copilotkit/react-core to ^1.53.0 (was ^1.51.2)
 > Updated @copilotkit/react-ui to ^1.53.0 (was ^1.51.2)
 > Updated @copilotkit/runtime to ^1.53.0 (was ^1.51.2)
 > Added @copilotkit/react-textarea 1.53.0
 > Updated next from 15.4.6 to 15.4.8
 > Updated packageManager

 -  update ag-ui submodule ([7f34bd1e73b1762](https://github.com/bsorrentino/langgraph4j/commit/7f34bd1e73b1762bcd07e01c33def466b5c08bee))
   
 -  **copilot-app**  update git ignore ([f91b5451e1ec36a](https://github.com/bsorrentino/langgraph4j/commit/f91b5451e1ec36a59fed23feb9f4b3e67d530059))
   
 -  **copilot-app**  update tsconfig.json ([89889182838eb24](https://github.com/bsorrentino/langgraph4j/commit/89889182838eb24f2415ec5499bc95d1bef7661e))
   
 -  **copilot-app**  update git ignore ([c494cc8fae6c3bc](https://github.com/bsorrentino/langgraph4j/commit/c494cc8fae6c3bc52561bd9442b4096307d5dea8))
   
 -  **copilot-app/package.json**  add pnpm packageManager field ([0389a11a579b24e](https://github.com/bsorrentino/langgraph4j/commit/0389a11a579b24eb0761cf42c66d56d12d8c5592))
   
 -  update ag-ui sdk submodule ([4c9e2fd2d0c7521](https://github.com/bsorrentino/langgraph4j/commit/4c9e2fd2d0c75210e1e7c3a4d80d5f1db72118a4))
   
 -  add build script ([e32ceb1540b25f3](https://github.com/bsorrentino/langgraph4j/commit/e32ceb1540b25f3d73ba83c3130d44987e615375))
   
 -  update ag-ui sdk submodule ([7328c921c85294c](https://github.com/bsorrentino/langgraph4j/commit/7328c921c85294c03bc166467d212e529c96eb2b))
   
 -  update git ignore ([6f7af16ff06d9d5](https://github.com/bsorrentino/langgraph4j/commit/6f7af16ff06d9d5910b54b415afbdf657fa94c9d))
   
 -  add build script helping to build all ([6a8a909194f0034](https://github.com/bsorrentino/langgraph4j/commit/6a8a909194f0034a5bc67b4381c27efe8b771e6f))
   
 -  promote project as multi-module ([8a76ec81efaf27d](https://github.com/bsorrentino/langgraph4j/commit/8a76ec81efaf27df88161f652fb0936ff5d7f1ad))
   
 -  move langgraph4j copilot implementation in a dedicated module ([4c210115d4f2f4d](https://github.com/bsorrentino/langgraph4j/commit/4c210115d4f2f4d2138533e86152a65dc2e073d0))
   
 -  add git submodule linked to https://github.com/ag-ui-protocol/ag-ui.git for use ag-ui-sdk ([e556c558ef10a38](https://github.com/bsorrentino/langgraph4j/commit/e556c558ef10a38365c19f837afa62739ee3fa6a))
   
 -  **pom**  Upgrade parent version to 1.8-SNAPSHOT ([885624c72a6cf59](https://github.com/bsorrentino/langgraph4j/commit/885624c72a6cf594662958b415c04eb50abf0411))
    > Update parent version in pom.xml to 1.8-SNAPSHOT

 -  **package**  Update dependencies to newer versions ([dc667d931c27948](https://github.com/bsorrentino/langgraph4j/commit/dc667d931c27948c1fca3371a6e0802b0825eac8))
    > Upgrade @copilotkit/react-core, @copilotkit/react-ui, @copilotkit/runtime, next, react and react-dom to latest versions

 -  bump langgraph4j to version 1.7-SNAPSHOT ([eceae0fc63f0684](https://github.com/bsorrentino/langgraph4j/commit/eceae0fc63f06843d14ed856011b8e765392107f))
   
 -  bump to 0.0.2-SNAPSHOT ([40f79571932f73f](https://github.com/bsorrentino/langgraph4j/commit/40f79571932f73f49886ccb49216dfc3a0fda504))
   

### Test 

 -  **AGUIAgentExecutor**  update AGUIAgentExecutor test to include streaming settings and remove redundant model parameter ([64bb1d22aa938e8](https://github.com/bsorrentino/langgraph4j/commit/64bb1d22aa938e8c2ec8f08e3b878405ba67bfe9))
   
 -  **AGUIAgentExecutor**  enable streaming and emit end event in agent executor test configuration ([c3da290461fd3a0](https://github.com/bsorrentino/langgraph4j/commit/c3da290461fd3a0560b802be02b87700c1e59a7d))
   





<!-- "name: v0.0.1" is a release tag -->

## [v0.0.1](https://github.com/bsorrentino/langgraph4j/releases/tag/v0.0.1) (2025-09-02)

### Features

 *  **AGUIAgentExecutor**  update approval for sendEmail ([f2ce3ff67dbd775](https://github.com/bsorrentino/langgraph4j/commit/f2ce3ff67dbd775a9c31341d96cfe10086fc1b36))
     > - Updated the approval mechanism to handle interruptions more flexibly using &#x60;InterruptionMetadata&#x60;.
     > work on #1
   
 *  **webui**  add support for approval ([5d27004a039477c](https://github.com/bsorrentino/langgraph4j/commit/5d27004a039477c5484e7c293ccff4d42afa88a7))
     > introduce useCopilotAction
     > work on #1
   
 *  add support for interruptions ([162756c633a673d](https://github.com/bsorrentino/langgraph4j/commit/162756c633a673d3de952591c4357d10adf3d2be))
     > work on #1
   
 *  add support for interruptions ([b20b1bb2e435f7d](https://github.com/bsorrentino/langgraph4j/commit/b20b1bb2e435f7d25350d376b5cce73b84dd2181))
     > work on #1
   


### Documentation

 -  update readme ([1187deb758a15bb](https://github.com/bsorrentino/langgraph4j/commit/1187deb758a15bb1486dcaf1a72c24d622b13877))

 -  update readme ([2c3868ff81157d9](https://github.com/bsorrentino/langgraph4j/commit/2c3868ff81157d957830ddd4770c999502df6785))

 -  update readme ([2bb28aef851b3b2](https://github.com/bsorrentino/langgraph4j/commit/2bb28aef851b3b2694b262e27338e03cbb184465))


### Refactor

 -  **webui**  Refactor message fetching logic to handle stream data efficiently ([29b1b6f2544132c](https://github.com/bsorrentino/langgraph4j/commit/29b1b6f2544132c7de04d4e42d400e4b9b0975bd))
   
 -  force use reactive runtime ([63caf13cdf07d4e](https://github.com/bsorrentino/langgraph4j/commit/63caf13cdf07d4e24dda1cb514163e444e62ace7))
   
 -  remve SSE implementation ([05bad8551f87c85](https://github.com/bsorrentino/langgraph4j/commit/05bad8551f87c85500287906755343686fe2fd5b))
   
 -  add abstract nodeOutputToText method ([17a0ec4e52c7d19](https://github.com/bsorrentino/langgraph4j/commit/17a0ec4e52c7d1918ecc13d5e432d76a04bb5e7f))
    > - refactor run method to use Flux over async stream

 -  updated the GraphData record to use CompiledGraph instead of StateGraph. ([e0e064dd7799191](https://github.com/bsorrentino/langgraph4j/commit/e0e064dd779919113952ea4ee68fdffefe06e5c3))
   
 -  change the AGUIAgent lookup strategy using @Qualifier name ([9a2e18c98e4bbaf](https://github.com/bsorrentino/langgraph4j/commit/9a2e18c98e4bbafb4cd844373f9fcab7427317d6))
   
 -  **AGUILangGraphAgent**  Make abstract methods protected ([3911546442e70b5](https://github.com/bsorrentino/langgraph4j/commit/3911546442e70b5dda7abb63de0233f960f20750))
    > Changed the visibility of buildStateGraph, buildGraphInput, and onInterruption methods from abstract to protected in AGUILangGraphAgent.java to better encapsulate their intended usage within subclasses.

 -  **AGUIAgentExecutor**  Add GITHUB_MODELS_GPT_4O_MINI model support and update agent executor ([38a7d3140052a7d](https://github.com/bsorrentino/langgraph4j/commit/38a7d3140052a7d6a9ba4800926e2f43a8a352c8))
   
 -  **AGUILangGraphAgent**  Add null check before checking if chunk() method is empty ([e7f124f15d9fb49](https://github.com/bsorrentino/langgraph4j/commit/e7f124f15d9fb49489334ef1b987c8423b856a9c))
   
 -  move Langgraph4jAdapter in a separate source file ([6151557b1475fc8](https://github.com/bsorrentino/langgraph4j/commit/6151557b1475fc833856fe4967162f0a047a67dd))
   
 -  add email confirmation details and styling improvements ([d777af95d6fbd10](https://github.com/bsorrentino/langgraph4j/commit/d777af95d6fbd100a89f521850d5fea637d339c0))
    > work on #1

 -  **chatApproval**  update button handlers to be compliant with approval results ([61231b2ba089d2c](https://github.com/bsorrentino/langgraph4j/commit/61231b2ba089d2cc8309f8eb7e496d350699e8e9))
    > work on #1

 -  **AGUIType**  add methods to retrieve last user and result messages ([9b1238df0bf16ab](https://github.com/bsorrentino/langgraph4j/commit/9b1238df0bf16ab7d388b3c52a17212453080e6c))
    > work on #1

 -  **AGUILangGraphAgent**  update state graph handling on interruption ([c17672cdbbb7173](https://github.com/bsorrentino/langgraph4j/commit/c17672cdbbb717385dd2688eccd54d5bbae1053a))
    > - Introduced &#x60;InterruptionMetadata&#x60; to capture interruption details
 > - Refactored state transition logic within &#x60;onInterruption&#x60;
 > - Updated state management and event emission flow
 > work on #1

 -  refine Events and Messages ([2c40ddcfcd657ba](https://github.com/bsorrentino/langgraph4j/commit/2c40ddcfcd657baa19ffc5eed849bdc24a3684bd))
    > work on #1


### ALM 

 -  add github actions ([5b0c060112a9c56](https://github.com/bsorrentino/langgraph4j/commit/5b0c060112a9c56a8e990c925eb4c90e0d9115e3))
   
 -  add changelog management ([6c1438307a341bc](https://github.com/bsorrentino/langgraph4j/commit/6c1438307a341bc15c6396339e1588c1f1275537))
   
 -  add github actions ([9a07d75f5223101](https://github.com/bsorrentino/langgraph4j/commit/9a07d75f5223101f1142bc128b56c82cd7a5feab))
   
 -  Add DNS resolver dependency profile for Apple Silicon ([c3888ca37b77623](https://github.com/bsorrentino/langgraph4j/commit/c3888ca37b77623e9214590187d02dcb0204acc0))
   
 -  update snapshot repository ID and URL ([a40f41bba468981](https://github.com/bsorrentino/langgraph4j/commit/a40f41bba4689814009cf85f3ff15666384a42de))
   

### Test 

 -  code refinements ([5c77b2257af6941](https://github.com/bsorrentino/langgraph4j/commit/5c77b2257af69417445090bb2310e54e421de8f7))
   





<!-- "name: v0.0.1-20250623" is a release tag -->

## [v0.0.1-20250623](https://github.com/bsorrentino/langgraph4j/releases/tag/v0.0.1-20250623) (2025-06-22)

### Features

 *  add langgraph4j  base implementation ([ba708614b32e115](https://github.com/bsorrentino/langgraph4j/commit/ba708614b32e11540df2e7efee36bb32d5001e75))
   
 *  **webui**  add Copilotkit powerd WEB-UI ([4207f1ca43d9a41](https://github.com/bsorrentino/langgraph4j/commit/4207f1ca43d9a41ef6efbe301b74e6349897ccb0))
   
 *  introduce new AGUIAbstractAgent and related interfaces ([cc5a7d9da117ff9](https://github.com/bsorrentino/langgraph4j/commit/cc5a7d9da117ff9d58a54bb1c083420cbe2c7a62))
     > - Added AGUIEvent interfaces
     > - Added AGUIMessage interfaces
     > - Introduced AGUISSEController to support SSE protocol.
     > - Added AGUIType interfaces
     > - Added SampleAgent class to AGUIAbstractAgent interface.
   
 *  introduce new AGUIAbstractAgent and related interfaces ([29146f7d161f1fd](https://github.com/bsorrentino/langgraph4j/commit/29146f7d161f1fdd5f4e7413f2fab4503a3c7d86))
     > - Added AGUIEvent interfaces
     > - Added AGUIMessage interfaces
     > - Introduced AGUISSEController to support SSE protocol.
     > - Added AGUIType interfaces
     > - Added SampleAgent class to AGUIAbstractAgent interface.
   

### Bug Fixes

 -  **webui**  update the fetchEvents retrieval process ([723cad298136e59](https://github.com/bsorrentino/langgraph4j/commit/723cad298136e59fd9962d2e6ec9f5ec6c87dff8))



### Refactor

 -  rename interface from AGUIAbstractAgent to AGUIAgent ([576a827f2e3d3cb](https://github.com/bsorrentino/langgraph4j/commit/576a827f2e3d3cbe1e7e636526bd896609f34861))
   
 -  **webui**  change internal route url ([fe867de79889859](https://github.com/bsorrentino/langgraph4j/commit/fe867de798898593d135a882c414814a107bccb9))
   

### ALM 

 -  adding required dependencies ([7a572e29596bc57](https://github.com/bsorrentino/langgraph4j/commit/7a572e29596bc57b4fc712c00e42b55aad3de1ef))
   
 -  init maven project ([345b53f0f245330](https://github.com/bsorrentino/langgraph4j/commit/345b53f0f245330a153fd166fcfd52a8e0859eba))
   

### Test 

 -  add test using AgentExecutor ([467438897dbc609](https://github.com/bsorrentino/langgraph4j/commit/467438897dbc60950cfac936399947a436f53304))
   



