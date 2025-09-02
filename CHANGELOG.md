# Changelog



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
   



