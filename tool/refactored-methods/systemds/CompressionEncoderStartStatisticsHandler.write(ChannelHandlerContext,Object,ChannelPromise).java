@Override
public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    ByteBuf byteBuf = (ByteBuf) msg;
    int initialSize = byteBuf.readableBytes();
    ctx.channel().attr(COMPRESSION_ENCODER_START_TIME_KEY).set(System.currentTimeMillis());
    ctx.channel().attr(INITIAL_SIZE_KEY).set(initialSize);
    super.write(ctx, msg, promise);
}