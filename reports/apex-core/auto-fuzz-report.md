# Differential-Fuzzing Report — apex-core

Generated 2026-08-13 05:23:21 · mode **fuzz** · **30s** per method · seed corpus **164** files · carried-over corpus **cleared**

- original tree: `/home/mhasan02/2ndSem/Differential-Fuzz-Testing/examples/apex-core/original`
- refactored tree: `/home/mhasan02/2ndSem/Differential-Fuzz-Testing/examples/apex-core/refactored`

## Summary

**27 classes · 86 changed methods**

| Outcome | Count | Meaning |
|---|---:|---|
| **EQUIVALENT** | 55 | no input made the two versions differ within the budget — trust it in proportion to the Confidence column |
| **DIVERGENT** | 16 | a concrete input makes them differ: a real behavioural change |
| **SKIP** | 15 | no verdict could be reached — see the breakdown below |

**71 of 86 methods got a verdict**; 15 could not be processed. Only EQUIVALENT and DIVERGENT are evidence about behaviour.

**Testing effort:** every method got a fixed budget of **30s**, in which the fuzzer generated **779,029 inputs** and completed **720,199 differential comparisons** (both versions invoked on the same input). The per-method counts are in the table below.

> `Inputs` is every input the fuzzer produced; `Compared` is only those that got as far as running BOTH versions. A large gap means most inputs died building a receiver or an argument, which is exactly when an EQUIVALENT should be doubted — and it is invisible in surefire's "Tests run", which counts JUnit invocations (each seed once, plus one call for the whole fuzzing session), not fuzz iterations.

### Why the SKIPs

| Reason | Count | What it means |
|---|---:|---|
| structurally untestable | 9 | no input could ever work: an abstract receiver with no concrete subtype, or a parameter type nothing can build |
| harness error | 4 | missing class at runtime, inaccessible member, or a bad manifest entry |
| never ran | 2 | the harness completed but no input ever built a receiver **and** arguments for **both** sides, so nothing was ever compared. Usually a fact about the refactoring — e.g. the refactored constructor throws. This is **not** equivalence |

> Fuzzing is **sound for non-equivalence and incomplete for equivalence**: a DIVERGENT is a witnessed fact, an EQUIVALENT is only "no counterexample found in the budget". That is what the Confidence column is for — it reports how much of the two versions the fuzzer actually reached, so an EQUIVALENT at 0/8 branches can be told apart from one at 8/8.

## Per method

Coverage is **differential**: `orig` is the `<Class>Original` snapshot, `ref` the `<Class>Refactored` one, both measured by Jazzer in the same run.

| Method | Kind | Verdict | Reason | Inputs | Compared | Branch orig | Branch ref | Line orig | Line ref | Confidence | Why |
|---|---|---|---|---:|---:|---|---|---|---|---|---|
| `AuthClient.authenticateMessage` | scalar | **SKIP** | structurally untestable | 2 | 0 | 0/12 | 0/4 | 0/11 | 0/3 | - | abstract/interface receiver with no concrete subtype on the classpath |
| `FastDataList.flush` | scalar | **EQUIVALENT** | - | 46 | 46 | 10/15 | 3/6 | 17/27 | 8/15 | 13/21 branches (62%) | no divergence found in 30s of fuzzing |
| `PhysicalNode.send` | object | **EQUIVALENT** | - | 316 | 127 | 5/6 | 2/2 | 6/7 | 4/4 | 7/8 branches (88%) | no divergence found in 30s of fuzzing |
| `PhysicalNode.unblock` | scalar | **EQUIVALENT** | - | 190 | 190 | 1/4 | 1/2 | 2/6 | 2/3 | only 2/6 branches (33%) — weak | no divergence found in 30s of fuzzing |
| `PhysicalNode.equals` | object | **EQUIVALENT** | - | 286 | 286 | 2/6 | 3/6 | 1/1 | 3/6 | only 5/12 branches (42%) — weak | no divergence found in 30s of fuzzing |
| `SubscribeRequestTuple.parse` | scalar | **EQUIVALENT** | - | 4,505 | 1 | 15/26 | 0/2 | 28/49 | 5/16 | only 15/28 branches (54%) — weak | no divergence found in 30s of fuzzing |
| `DiskStorage.ctor` | ctor | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 0/4 | 0/4 | witnessed | **constructed state: basePath: /tmp vs /tmp/msp2157808635607949530** — on `[]` original returns com.datatorrent.bufferserv, refactored returns com.datatorrent.bufferserv |
| `DiskStorage.normalizeFileName` | scalar | **EQUIVALENT** | - | 45,383 | 45,383 | 4/4 | 4/4 | 7/7 | 6/6 | all 8 branches exercised | no divergence found in 30s of fuzzing |
| `DiskStorage.store` | scalar | **DIVERGENT** | - | 66 | 66 | 8/8 | 5/8 | 25/28 | 14/28 | witnessed | **exception type** — on `[\u0000\u0003\u0000\u0000\u0008\u0000\u0000\` original throws IllegalStateException, refactored returns 1 |
| `DiskStorage.discard` | scalar | **EQUIVALENT** | - | 30,273 | 30,273 | 9/12 | 3/12 | 15/21 | 7/21 | only 12/24 branches (50%) — weak | no divergence found in 30s of fuzzing |
| `DiskStorage.retrieve` | scalar | **EQUIVALENT** | - | 30,506 | 30,506 | 7/10 | 3/10 | 13/16 | 7/16 | only 10/20 branches (50%) — weak | no divergence found in 30s of fuzzing |
| `System.startup` | scalar | **EQUIVALENT** | - | 63,136 | 63,136 | 2/2 | 2/2 | 8/10 | 7/7 | all 4 branches exercised | no divergence found in 30s of fuzzing |
| `System.shutdown` | scalar | **EQUIVALENT** | - | 47,438 | 47,438 | 1/2 | 1/2 | 4/7 | 4/7 | only 2/4 branches (50%) — weak | no divergence found in 30s of fuzzing |
| `StringCodecs.loadDefaultConverters` | scalar | **EQUIVALENT** | - | 45,338 | 45,338 | 0/0 | 0/0 | 4/4 | 4/4 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `StringCodecs.check` | scalar | **EQUIVALENT** | - | 45,608 | 45,608 | 3/4 | 3/4 | 4/11 | 4/10 | 6/8 branches (75%) | no divergence found in 30s of fuzzing |
| `StringCodecs.register` | object | **EQUIVALENT** | - | 61,966 | 37,472 | 0/0 | 0/0 | 1/5 | 1/5 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `FSPartFileAgent.getNextPartFile` | scalar | **EQUIVALENT** | - | 46,115 | 46,115 | 4/6 | 2/4 | 3/5 | 3/5 | 6/10 branches (60%) | no divergence found in 30s of fuzzing |
| `PermissionsInfo.ctor` | ctor | **EQUIVALENT** | - | 865 | 865 | 0/0 | 0/0 | 5/8 | 2/8 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `PermissionsInfo.addReadWriteUser` | scalar | **DIVERGENT** | - | 6 | 6 | 0/0 | 0/0 | 2/2 | 2/2 | witnessed | **receiver state after call: readWriteRoles: TreeSet[roles] vs TreeSet[]** — on `[roles]` original returns null, refactored returns null |
| `PermissionsInfo.toJSONObject` | scalar | **EQUIVALENT** | - | 3,047 | 3,047 | 0/0 | 0/0 | 15/15 | 5/7 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `WebServicesVersionConversion.isVersionCompatible` | scalar | **DIVERGENT** | - | 11 | 11 | 4/4 | 2/4 | 10/10 | 5/10 | witnessed | **exception type** — on `[v48]` original returns false, refactored throws NumberFormatException |
| `WebServicesVersionConversion.getConverter` | scalar | **DIVERGENT** | - | 10 | 10 | 4/4 | 1/4 | 5/5 | 2/5 | witnessed | **exception type** — on `[v1]` original returns com.datatorrent.stram.clie, refactored throws NumberFormatException |
| `MuxReservoir.acquireReservoir` | scalar | **SKIP** | structurally untestable | 2 | 0 | 0/4 | 0/2 | 0/9 | 0/5 | - | abstract/interface receiver with no concrete subtype on the classpath |
| `MuxReservoir.releaseReservoir` | scalar | **SKIP** | structurally untestable | 2 | 0 | 0/6 | 0/2 | 0/9 | 0/4 | - | abstract/interface receiver with no concrete subtype on the classpath |
| `Slider.process` | object | **EQUIVALENT** | - | 689 | 654 | 2/2 | 2/2 | 1/4 | 4/4 | all 4 branches exercised | no divergence found in 30s of fuzzing |
| `Slider.beginWindow` | scalar | **EQUIVALENT** | - | 333 | 306 | 1/6 | 2/2 | 5/10 | 5/6 | only 3/8 branches (38%) — weak | no divergence found in 30s of fuzzing |
| `Slider.endWindow` | scalar | **EQUIVALENT** | - | 427 | 398 | 4/4 | 2/2 | 6/6 | 3/5 | all 6 branches exercised | no divergence found in 30s of fuzzing |
| `Slider.setup` | object | **EQUIVALENT** | - | 3,330 | 2,602 | 0/0 | 0/0 | 3/5 | 0/5 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `Slider.handleIdleTime` | scalar | **EQUIVALENT** | - | 451 | 391 | 2/2 | 2/2 | 5/7 | 4/4 | all 4 branches exercised | no divergence found in 30s of fuzzing |
| `WindowIdActivatedReservoir.remove` | scalar | **EQUIVALENT** | - | 10,775 | 394 | 2/2 | 2/2 | 3/4 | 3/3 | all 4 branches exercised | no divergence found in 30s of fuzzing |
| `WindowIdActivatedReservoir.setSink` | object | **DIVERGENT** | - | 3,096 | 1 | 0/0 | 0/0 | 2/2 | 2/2 | witnessed | **return value: <value>: null vs com.datatorrent.stram.debug.MuxSink@4dbad37** — on `[com.datatorrent.stram.debug.MuxSink@7b4acdc` original returns null, refactored returns com.datatorrent.stram.debu |
| `WindowIdActivatedReservoir.sweep` | scalar | **EQUIVALENT** | - | 10,351 | 516 | 2/6 | 0/4 | 4/6 | 0/6 | only 2/10 branches (20%) — weak | no divergence found in 30s of fuzzing |
| `KerberosAuth.loginUser` | scalar | **EQUIVALENT** | - | 10,562 | 10,562 | 0/0 | 0/0 | 2/4 | 2/4 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `FastSubscriber.readSize` | scalar | **EQUIVALENT** | - | 203 | 202 | 1/2 | 1/2 | 2/4 | 2/3 | only 2/4 branches (50%) — weak | no divergence found in 30s of fuzzing |
| `InlineStream.put` | object | **EQUIVALENT** | - | 213 | 193 | 1/2 | 0/0 | 5/8 | 4/6 | only 1/2 branches (50%) — weak | no divergence found in 30s of fuzzing |
| `InlineStream.getCount` | scalar | **EQUIVALENT** | - | 336 | 234 | 2/2 | 0/0 | 3/3 | 3/3 | all 2 branches exercised | no divergence found in 30s of fuzzing |
| `MuxStream.teardown` | scalar | **EQUIVALENT** | - | 1,982 | 1,982 | 0/0 | 0/0 | 2/2 | 3/3 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `MuxStream.activate` | object | **EQUIVALENT** | - | 189 | 189 | 1/2 | 0/0 | 5/7 | 2/2 | only 1/2 branches (50%) — weak | no divergence found in 30s of fuzzing |
| `MuxStream.setSink` | object | **DIVERGENT** | - | 7 | 7 | 2/6 | 1/2 | 4/8 | 4/5 | witnessed | **receiver state after call: sinks: [] vs [MuxSink{count=0,sinks=[]}]** — on `[, com.datatorrent.stram.debug.MuxSink@7c28c` original returns null, refactored returns null |
| `ConfigValidator.validateLoggersLevel` | scalar | **EQUIVALENT** | - | 135 | 135 | 4/4 | 4/4 | 5/5 | 1/1 | all 8 branches exercised | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.ctor` | ctor | **EQUIVALENT** | - | 217 | 217 | 0/0 | 0/0 | 4/4 | 0/3 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.ctor_1` | ctor | **EQUIVALENT** | - | 32,323 | 32,323 | 0/0 | 0/0 | 0/4 | 0/3 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.ctor_2` | ctor | **SKIP** | structurally untestable | 2 | 0 | 0/0 | 0/0 | 0/4 | 0/3 | - | constructor parameter has no buildable form: java.util.Comparator |
| `StablePriorityQueue.ctor_3` | ctor | **EQUIVALENT** | - | 172 | 172 | 0/0 | 0/0 | 4/4 | 0/3 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.element` | scalar | **EQUIVALENT** | - | 31 | 28 | 0/0 | 0/0 | 3/4 | 3/4 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.peek` | scalar | **EQUIVALENT** | - | 295 | 25 | 1/2 | 0/2 | 3/4 | 0/2 | only 1/4 branches (25%) — weak | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.remove` | scalar | **EQUIVALENT** | - | 460 | 10 | 0/0 | 0/0 | 3/4 | 0/4 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.poll` | scalar | **EQUIVALENT** | - | 333 | 12 | 1/2 | 0/2 | 3/4 | 0/2 | only 1/4 branches (25%) — weak | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.comparator` | scalar | **SKIP** | never ran | 256 | 0 | 1/2 | 0/2 | 3/4 | 0/2 | - | never built on both sides — original: neither autofuzz nor constructor synthesis built com.datatorrent.stram.util.StablePriorityQueueOrigi (NOT equivalence) |
| `StablePriorityQueue.size` | scalar | **EQUIVALENT** | - | 363 | 18 | 1/2 | 0/2 | 4/4 | 0/4 | only 1/4 branches (25%) — weak | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.isEmpty` | scalar | **EQUIVALENT** | - | 499 | 28 | 1/2 | 0/2 | 4/4 | 0/4 | only 1/4 branches (25%) — weak | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.contains` | object | **EQUIVALENT** | - | 321 | 21 | 1/4 | 1/4 | 2/5 | 2/5 | only 2/8 branches (25%) — weak | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.toArray` | scalar | **EQUIVALENT** | - | 275 | 22 | 2/2 | 1/2 | 4/4 | 3/4 | 3/4 branches (75%) | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.toArray_1` | object | **SKIP** | never ran | 340 | 0 | 0/2 | 0/2 | 0/4 | 0/4 | - | never built on both sides — original: neither autofuzz nor constructor synthesis built com.datatorrent.stram.util.StablePriorityQueueOrigi (NOT equivalence) |
| `StablePriorityQueue.remove_1` | object | **EQUIVALENT** | - | 260 | 13 | 0/0 | 0/0 | 0/4 | 0/4 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.addAll` | object | **EQUIVALENT** | - | 299 | 20 | 4/8 | 0/8 | 5/10 | 0/8 | only 4/16 branches (25%) — weak | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.removeAll` | object | **EQUIVALENT** | - | 269 | 37 | 7/14 | 4/14 | 9/15 | 6/15 | only 11/28 branches (39%) — weak | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.retainAll` | object | **EQUIVALENT** | - | 153 | 134 | 2/8 | 3/10 | 4/10 | 5/12 | only 5/18 branches (28%) — weak | no divergence found in 30s of fuzzing |
| `StablePriorityQueue.clear` | scalar | **EQUIVALENT** | - | 112 | 92 | 0/0 | 0/0 | 3/3 | 3/3 | branchless (judge on Line) | no divergence found in 30s of fuzzing |
| `StreamGobbler.run` | scalar | **EQUIVALENT** | - | 32,627 | 32,627 | 4/4 | 0/0 | 9/11 | 4/6 | all 4 branches exercised | no divergence found in 30s of fuzzing |
| `VersionInfo.ctor` | ctor | **EQUIVALENT** | - | 21,617 | 20,119 | 8/8 | 0/0 | 33/34 | 11/12 | all 8 branches exercised | no divergence found in 30s of fuzzing |
| `VersionInfo.compare` | scalar | **DIVERGENT** | - | 10 | 10 | 14/14 | 10/10 | 13/13 | 7/9 | witnessed | **exception type** — on `[ by , 5zgRu$OxE#KCir#gA]` original returns -1, refactored throws NumberFormatException |
| `VersionInfo.isCompatible` | scalar | **EQUIVALENT** | - | 41,413 | 41,413 | 4/4 | 3/4 | 7/7 | 6/7 | 7/8 branches (88%) | no divergence found in 30s of fuzzing |
| `BaseSignatureVisitor.visitBaseType` | scalar | **SKIP** | structurally untestable | 2 | 0 | 0/0 | 0/0 | 0/5 | 0/5 | - | abstract/interface receiver with no concrete subtype on the classpath |
| `BaseSignatureVisitor.visitClassType` | scalar | **SKIP** | structurally untestable | 2 | 0 | 0/0 | 0/0 | 0/4 | 0/4 | - | abstract/interface receiver with no concrete subtype on the classpath |
| `BaseSignatureVisitor.resolveStack` | scalar | **SKIP** | structurally untestable | 2 | 0 | 0/12 | 0/10 | 0/19 | 0/17 | - | abstract/interface receiver with no concrete subtype on the classpath |
| `BaseSignatureVisitor.visitTypeArgument` | scalar | **SKIP** | structurally untestable | 2 | 0 | 0/0 | 0/0 | 0/4 | 0/4 | - | abstract/interface receiver with no concrete subtype on the classpath |
| `BaseSignatureVisitor.visitTypeVariable` | scalar | **SKIP** | structurally untestable | 2 | 0 | 0/6 | 0/2 | 0/13 | 0/6 | - | abstract/interface receiver with no concrete subtype on the classpath |
| `ClassSignatureVisitor.visitExceptionType` | scalar | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 1/1 | 1/1 | witnessed | **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns com.datatorrent.stram.weba |
| `ClassSignatureVisitor.visitParameterType` | scalar | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 1/1 | 1/1 | witnessed | **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns com.datatorrent.stram.weba |
| `ClassSignatureVisitor.visitReturnType` | scalar | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 1/1 | 1/1 | witnessed | **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns com.datatorrent.stram.weba |
| `ClassSignatureVisitor.visitSuperclass` | scalar | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 3/3 | 3/3 | witnessed | **return value: end: SUPERCLASS vs SUPERCLASS** — on `[]` original returns com.datatorrent.stram.weba, refactored returns com.datatorrent.stram.weba |
| `ClassSignatureVisitor.visitInterface` | scalar | **DIVERGENT** | - | 3 | 3 | 3/8 | 0/0 | 6/8 | 4/4 | witnessed | **return value: end: INTERFACE vs INTERFACE** — on `[]` original returns com.datatorrent.stram.weba, refactored returns com.datatorrent.stram.weba |
| `ClassSignatureVisitor.getInterfaces` | scalar | **EQUIVALENT** | - | 31,808 | 31,808 | 5/8 | 0/0 | 4/5 | 3/3 | 5/8 branches (62%) | no divergence found in 30s of fuzzing |
| `ClassSignatureVisitor.getSuperClass` | scalar | **EQUIVALENT** | - | 32,237 | 32,237 | 3/4 | 0/0 | 2/3 | 2/2 | 3/4 branches (75%) | no divergence found in 30s of fuzzing |
| `FieldSignatureVisitor.visitExceptionType` | scalar | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 1/1 | 1/1 | witnessed | **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns null |
| `FieldSignatureVisitor.visitParameterType` | scalar | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 1/1 | 1/1 | witnessed | **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns null |
| `FieldSignatureVisitor.visitReturnType` | scalar | **DIVERGENT** | - | 3 | 3 | 0/0 | 0/0 | 1/1 | 1/1 | witnessed | **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns null |
| `FieldSignatureVisitor.getFieldType` | scalar | **EQUIVALENT** | - | 27,175 | 27,175 | 2/2 | 1/2 | 3/5 | 3/3 | 3/4 branches (75%) | no divergence found in 30s of fuzzing |
| `MethodSignatureVisitor.visitExceptionType` | scalar | **EQUIVALENT** | - | 22,104 | 22,104 | 6/8 | 1/6 | 4/6 | 3/7 | only 7/14 branches (50%) — weak | no divergence found in 30s of fuzzing |
| `MethodSignatureVisitor.visitParameterType` | scalar | **EQUIVALENT** | - | 23,785 | 23,785 | 3/6 | 2/6 | 7/9 | 4/6 | only 5/12 branches (42%) — weak | no divergence found in 30s of fuzzing |
| `PropertiesHelper.getLong` | scalar | **EQUIVALENT** | - | 41,032 | 41,032 | 1/6 | 1/4 | 5/12 | 5/12 | only 2/10 branches (20%) — weak | no divergence found in 30s of fuzzing |
| `GroupingManager.getEventGroupIdForAffectedContainer` | scalar | **SKIP** | harness error | 2 | 0 | 0/6 | 0/2 | 0/8 | 0/4 | - | NoClassDefFoundError — could not be tested |
| `GroupingManager.getEventGroupIdForOperatorToDeploy` | scalar | **SKIP** | harness error | 2 | 0 | 0/4 | 0/0 | 0/5 | 0/1 | - | NoClassDefFoundError — could not be tested |
| `GroupingManager.removeOperatorFromGroupingRequest` | scalar | **SKIP** | harness error | 2 | 0 | 0/4 | 0/0 | 0/5 | 0/1 | - | NoClassDefFoundError — could not be tested |
| `GroupingManager.moveOperatorFromUndeployListToDeployList` | object | **SKIP** | harness error | 2 | 0 | 0/4 | 0/0 | 0/8 | 0/1 | - | NoClassDefFoundError — could not be tested |

## Divergences

- **`DiskStorage.ctor`** — **constructed state: basePath: /tmp vs /tmp/msp2157808635607949530** — on `[]` original returns com.datatorrent.bufferserv, refactored returns com.datatorrent.bufferserv
- **`DiskStorage.store`** — **exception type** — on `[\u0000\u0003\u0000\u0000\u0008\u0000\u0000\` original throws IllegalStateException, refactored returns 1
- **`PermissionsInfo.addReadWriteUser`** — **receiver state after call: readWriteRoles: TreeSet[roles] vs TreeSet[]** — on `[roles]` original returns null, refactored returns null
- **`WebServicesVersionConversion.isVersionCompatible`** — **exception type** — on `[v48]` original returns false, refactored throws NumberFormatException
- **`WebServicesVersionConversion.getConverter`** — **exception type** — on `[v1]` original returns com.datatorrent.stram.clie, refactored throws NumberFormatException
- **`WindowIdActivatedReservoir.setSink`** — **return value: <value>: null vs com.datatorrent.stram.debug.MuxSink@4dbad37** — on `[com.datatorrent.stram.debug.MuxSink@7b4acdc` original returns null, refactored returns com.datatorrent.stram.debu
- **`MuxStream.setSink`** — **receiver state after call: sinks: [] vs [MuxSink{count=0,sinks=[]}]** — on `[, com.datatorrent.stram.debug.MuxSink@7c28c` original returns null, refactored returns null
- **`VersionInfo.compare`** — **exception type** — on `[ by , 5zgRu$OxE#KCir#gA]` original returns -1, refactored throws NumberFormatException
- **`ClassSignatureVisitor.visitExceptionType`** — **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns com.datatorrent.stram.weba
- **`ClassSignatureVisitor.visitParameterType`** — **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns com.datatorrent.stram.weba
- **`ClassSignatureVisitor.visitReturnType`** — **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns com.datatorrent.stram.weba
- **`ClassSignatureVisitor.visitSuperclass`** — **return value: end: SUPERCLASS vs SUPERCLASS** — on `[]` original returns com.datatorrent.stram.weba, refactored returns com.datatorrent.stram.weba
- **`ClassSignatureVisitor.visitInterface`** — **return value: end: INTERFACE vs INTERFACE** — on `[]` original returns com.datatorrent.stram.weba, refactored returns com.datatorrent.stram.weba
- **`FieldSignatureVisitor.visitExceptionType`** — **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns null
- **`FieldSignatureVisitor.visitParameterType`** — **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns null
- **`FieldSignatureVisitor.visitReturnType`** — **exception type** — on `[]` original throws UnsupportedOperationExcepti, refactored returns null

Reproduce with:
```bash
grep -a -A6 -F '[DIFFERENTIAL MISMATCH]' target/fuzz-logs/apex-core/<Class>_<method>.log
```

## EQUIVALENT verdicts to distrust

These were never contradicted, but the fuzzer reached too little of the code for that to mean much. Raise `--duration`, or improve the seed corpus.

- `PhysicalNode.unblock` — only 2/6 branches (33%) — weak
- `PhysicalNode.equals` — only 5/12 branches (42%) — weak
- `SubscribeRequestTuple.parse` — only 15/28 branches (54%) — weak
- `DiskStorage.discard` — only 12/24 branches (50%) — weak
- `DiskStorage.retrieve` — only 10/20 branches (50%) — weak
- `System.shutdown` — only 2/4 branches (50%) — weak
- `Slider.beginWindow` — only 3/8 branches (38%) — weak
- `WindowIdActivatedReservoir.sweep` — only 2/10 branches (20%) — weak
- `FastSubscriber.readSize` — only 2/4 branches (50%) — weak
- `InlineStream.put` — only 1/2 branches (50%) — weak
- `MuxStream.activate` — only 1/2 branches (50%) — weak
- `StablePriorityQueue.peek` — only 1/4 branches (25%) — weak
- `StablePriorityQueue.poll` — only 1/4 branches (25%) — weak
- `StablePriorityQueue.size` — only 1/4 branches (25%) — weak
- `StablePriorityQueue.isEmpty` — only 1/4 branches (25%) — weak
- `StablePriorityQueue.contains` — only 2/8 branches (25%) — weak
- `StablePriorityQueue.addAll` — only 4/16 branches (25%) — weak
- `StablePriorityQueue.removeAll` — only 11/28 branches (39%) — weak
- `StablePriorityQueue.retainAll` — only 5/18 branches (28%) — weak
- `MethodSignatureVisitor.visitExceptionType` — only 7/14 branches (50%) — weak
- `MethodSignatureVisitor.visitParameterType` — only 5/12 branches (42%) — weak
- `PropertiesHelper.getLong` — only 2/10 branches (20%) — weak

<!-- config: seeds=164 duration=30s mode=fuzz keepcorpus=False coverage=jazzer -->
