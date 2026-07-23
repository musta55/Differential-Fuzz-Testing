# Differential-Fuzzing Report — apex-core (qwen)

- Generated: 2026-06-28 17:53:48  ·  Mode: **fuzz**  ·  1m/method
- 62 auto-fuzzable methods (manifest-driven; original=<Class>Original, refactored=<Class>Refactored).
- Branch/Line = JaCoCo per-method coverage. DIVERGENT = exception-type or return-value mismatch (one-sided TIMEOUT is NOT counted).

| Method | Result | Tests (fail) | Branch | Line | Evidence |
|--------|--------|--------------|--------|------|----------|
| AuthClient.authenticateMessage | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |
| BaseSignatureVisitor.visitBaseType | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |
| BaseSignatureVisitor.visitClassType | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |
| BaseSignatureVisitor.resolveStack | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |
| BaseSignatureVisitor.visitTypeArgument | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |
| BaseSignatureVisitor.visitTypeVariable | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |
| ClassSignatureVisitor.visitExceptionType | **DIVERGENT** | 3 (3) | 0/0 | 1/1 | orig: throws UnsupportedOperationExc vs ref: returns com.datatorrent.stram. |
| ClassSignatureVisitor.visitParameterType | **DIVERGENT** | 3 (3) | 0/0 | 1/1 | orig: throws UnsupportedOperationExc vs ref: returns com.datatorrent.stram. |
| ClassSignatureVisitor.visitReturnType | **DIVERGENT** | 3 (3) | 0/0 | 1/1 | orig: throws UnsupportedOperationExc vs ref: returns com.datatorrent.stram. |
| ClassSignatureVisitor.visitSuperclass | **EQUIVALENT** | 2 (0) | 0/0 | 3/3 | no divergence in 1m |
| ClassSignatureVisitor.visitInterface | **EQUIVALENT** | 2 (0) | 0/0 | 4/4 | no divergence in 1m |
| ClassSignatureVisitor.getInterfaces | **EQUIVALENT** | 2 (0) | 0/0 | 3/3 | no divergence in 1m |
| ClassSignatureVisitor.getSuperClass | **EQUIVALENT** | 2 (0) | 0/0 | 2/2 | no divergence in 1m |
| ConfigValidator.validateLoggersLevel | **EQUIVALENT** | 2 (0) | 3/4 | 1/1 | no divergence in 1m |
| DiskStorage.normalizeFileName | **EQUIVALENT** | 2 (0) | 4/4 | 6/6 | no divergence in 1m |
| DiskStorage.store | **DIVERGENT** | 3 (2) | 4/8 | 13/28 | orig: throws IllegalStateException vs ref: returns 1 |
| DiskStorage.discard | **EQUIVALENT** | 2 (0) | 3/12 | 7/21 | no divergence in 1m |
| DiskStorage.retrieve | **EQUIVALENT** | 2 (0) | 3/10 | 7/16 | no divergence in 1m |
| FSPartFileAgent.getNextPartFile | **EQUIVALENT** | 2 (0) | 3/4 | 4/5 | no divergence in 1m |
| FastDataList.flush | **EQUIVALENT** | 3 (0) | 3/6 | 8/15 | no divergence in 1m |
| FastSubscriber.readSize | **EQUIVALENT** | 3 (0) | 1/2 | 2/3 | no divergence in 1m |
| FieldSignatureVisitor.visitExceptionType | **DIVERGENT** | 3 (3) | 0/0 | 1/1 | orig: throws UnsupportedOperationExc vs ref: returns null |
| FieldSignatureVisitor.visitParameterType | **DIVERGENT** | 3 (3) | 0/0 | 1/1 | orig: throws UnsupportedOperationExc vs ref: returns null |
| FieldSignatureVisitor.visitReturnType | **DIVERGENT** | 3 (3) | 0/0 | 1/1 | orig: throws UnsupportedOperationExc vs ref: returns null |
| FieldSignatureVisitor.getFieldType | **EQUIVALENT** | 2 (0) | 1/2 | 2/3 | no divergence in 1m |
| GroupingManager.getEventGroupIdForAffectedContainer | **ERROR** | 3 (3) | 0/2 | 0/4 | NoClassDefFoundError (skip) |
| GroupingManager.getEventGroupIdForOperatorToDeploy | **ERROR** | 3 (3) | 0/0 | 0/1 | NoClassDefFoundError (skip) |
| GroupingManager.removeOperatorFromGroupingRequest | **ERROR** | 3 (3) | 0/0 | 0/1 | NoClassDefFoundError (skip) |
| InlineStream.getCount | **EQUIVALENT** | 4 (0) | 0/0 | 3/3 | no divergence in 1m |
| KerberosAuth.loginUser | **EQUIVALENT** | 2 (0) | 0/0 | 2/4 | no divergence in 1m |
| MethodSignatureVisitor.visitExceptionType | **EQUIVALENT** | 2 (0) | 1/6 | 3/7 | no divergence in 1m |
| MethodSignatureVisitor.visitParameterType | **EQUIVALENT** | 2 (0) | 2/6 | 4/6 | no divergence in 1m |
| MuxReservoir.acquireReservoir | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |
| MuxReservoir.releaseReservoir | **SKIP** | - (-) | n/a | n/a | instance, no no-arg ctor |
| MuxStream.teardown | **EQUIVALENT** | 2 (0) | 0/0 | 3/3 | no divergence in 1m |
| PermissionsInfo.addReadWriteUser | **EQUIVALENT** | 2 (0) | 0/0 | 2/2 | no divergence in 1m |
| PermissionsInfo.toJSONObject | **EQUIVALENT** | 2 (0) | 0/0 | 5/7 | no divergence in 1m |
| PhysicalNode.unblock | **EQUIVALENT** | 2 (0) | 1/2 | 2/3 | no divergence in 1m |
| PropertiesHelper.getLong | **EQUIVALENT** | 2 (0) | 1/4 | 5/12 | no divergence in 1m |
| Slider.beginWindow | **SKIP** | 2 (2) | 0/2 | 0/6 | no no-arg ctor (instance) |
| Slider.endWindow | **SKIP** | 2 (2) | 0/2 | 0/5 | no no-arg ctor (instance) |
| Slider.handleIdleTime | **SKIP** | 2 (2) | 0/2 | 0/4 | no no-arg ctor (instance) |
| StablePriorityQueue.element | **EQUIVALENT** | 3 (0) | 0/0 | 3/4 | no divergence in 1m |
| StablePriorityQueue.peek | **EQUIVALENT** | 3 (0) | 1/2 | 2/2 | no divergence in 1m |
| StablePriorityQueue.remove | **EQUIVALENT** | 3 (0) | 0/0 | 3/4 | no divergence in 1m |
| StablePriorityQueue.poll | **EQUIVALENT** | 4 (0) | 1/2 | 2/2 | no divergence in 1m |
| StablePriorityQueue.comparator | **EQUIVALENT** | 4 (0) | 1/2 | 2/2 | no divergence in 1m |
| StablePriorityQueue.size | **EQUIVALENT** | 3 (0) | 1/2 | 4/4 | no divergence in 1m |
| StablePriorityQueue.isEmpty | **EQUIVALENT** | 4 (0) | 1/2 | 4/4 | no divergence in 1m |
| StablePriorityQueue.clear | **EQUIVALENT** | 4 (0) | 0/0 | 3/3 | no divergence in 1m |
| StreamGobbler.run | **SKIP** | 2 (2) | 0/0 | 0/6 | no no-arg ctor (instance) |
| StringCodecs.loadDefaultConverters | **EQUIVALENT** | 2 (0) | 0/0 | 4/4 | no divergence in 1m |
| StringCodecs.check | **EQUIVALENT** | 2 (0) | 3/4 | 4/10 | no divergence in 1m |
| SubscribeRequestTuple.parse | **DIVERGENT** | 3 (2) | 0/2 | 5/16 | orig: returns null vs ref: throws StringIndexOutOfBoundsE |
| System.startup | **EQUIVALENT** | 2 (0) | 2/2 | 7/7 | no divergence in 1m |
| System.shutdown | **EQUIVALENT** | 2 (0) | 1/2 | 4/7 | no divergence in 1m |
| VersionInfo.compare | **DIVERGENT** | 3 (2) | 8/10 | 7/9 | orig: returns 0 vs ref: throws NumberFormatException |
| VersionInfo.isCompatible | **EQUIVALENT** | 2 (0) | 4/4 | 7/7 | no divergence in 1m |
| WebServicesVersionConversion.isVersionCompatible | **DIVERGENT** | 3 (2) | 2/4 | 5/10 | orig: returns true vs ref: throws NumberFormatException |
| WebServicesVersionConversion.getConverter | **DIVERGENT** | 3 (2) | 1/4 | 2/5 | orig: returns null vs ref: throws NumberFormatException |
| WindowIdActivatedReservoir.remove | **SKIP** | 2 (2) | 0/2 | 0/3 | no no-arg ctor (instance) |
| WindowIdActivatedReservoir.sweep | **SKIP** | 2 (2) | 0/4 | 0/6 | no no-arg ctor (instance) |

## Summary

- EQUIVALENT **34** · DIVERGENT **11** · SKIP **14** · ERROR **3**  (62 methods)
- SKIP = instance method with no no-arg constructor (receiver can't be built generically).
