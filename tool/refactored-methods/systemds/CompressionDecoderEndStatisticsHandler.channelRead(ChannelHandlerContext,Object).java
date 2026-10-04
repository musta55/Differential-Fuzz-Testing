@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    AttributeKey<Long> startTimeKey = AttributeKey.valueOf("compressionDecoderStartTime");
    long startTime = ctx.channel().attr(startTimeKey).get();
    long elapsedTime = System.currentTimeMillis() - startTime;
    ByteBuf byteBuf = (ByteBuf) msg;
    long finalSize = byteBuf.readableBytes();
    AttributeKey<Integer> initialSizeKey = AttributeKey.valueOf("initialSize");
    long initialSize = ctx.channel().attr(initialSizeKey).get();
    FederatedCompressionStatistics.decodingStep(elapsedTime, initialSize, finalSize);
    super.channelRead(ctx, msg);
}