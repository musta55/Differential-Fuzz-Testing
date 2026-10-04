@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    long startTime = ctx.channel().attr(AttributeKey.<Long>valueOf("compressionDecoderStartTime")).get();
    long elapsedTime = System.currentTimeMillis() - startTime;
    ByteBuf byteBuf = (ByteBuf) msg;
    long finalSize = byteBuf.readableBytes();
    long initialSize = ctx.channel().attr(AttributeKey.<Integer>valueOf("initialSize")).get();
    FederatedCompressionStatistics.decodingStep(elapsedTime, initialSize, finalSize);
    super.channelRead(ctx, msg);
}