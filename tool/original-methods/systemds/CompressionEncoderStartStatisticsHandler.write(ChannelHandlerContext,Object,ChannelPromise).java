@Override
public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    ByteBuf byteBuf = (ByteBuf) msg;
    int initialSize = byteBuf.readableBytes();
    ctx.channel().attr(AttributeKey.valueOf("compressionEncoderStartTime")).set(System.currentTimeMillis());
    ctx.channel().attr(AttributeKey.valueOf("initialSize")).set(initialSize);
    super.write(ctx, msg, promise);
}