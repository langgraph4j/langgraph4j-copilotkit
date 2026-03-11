# Changelog



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
   



