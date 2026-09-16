public ThreadGroupGui(boolean showDelayedStart) {
    super();
    this.showDelayedStart = showDelayedStart;
    init();
    initGui();
    if (showDelayedStart) {
        bindingGroup.add(delayedStart);
    }
    bindingGroup.addAll(Arrays.asList(new JTextComponentBinding(threadInput, AbstractThreadGroupSchema.INSTANCE.getNumThreads()), new JTextComponentBinding(rampInput, ThreadGroupSchema.INSTANCE.getRampTime()), new JTextComponentBinding(duration, ThreadGroupSchema.INSTANCE.getDuration()), new JTextComponentBinding(delay, ThreadGroupSchema.INSTANCE.getDelay()), sameUserBox, scheduler));
}