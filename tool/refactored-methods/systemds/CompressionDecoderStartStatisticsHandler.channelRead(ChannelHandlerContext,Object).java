@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf byteBuf = (ByteBuf) msg;
    int initialSize = byteBuf.readableBytes();
    ctx.channel().attr(COMPRESSION_DECODER_START_TIME_KEY).set(System.currentTimeMillis());
    ctx.channel().attr(INITIAL_SIZE_KEY).set(initialSize);
    super.channelRead(ctx, msg);
}