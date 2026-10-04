@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf byteBuf = (ByteBuf) msg;
    int initialSize = byteBuf.readableBytes();
    ctx.channel().attr(AttributeKey.valueOf("compressionDecoderStartTime")).set(System.currentTimeMillis());
    ctx.channel().attr(AttributeKey.valueOf("initialSize")).set(initialSize);
    super.channelRead(ctx, msg);
}