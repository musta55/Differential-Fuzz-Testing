/**
 * This method will receive WB as binary data (png) and store it to temporary PDF/PNG file
 *
 * unlike other web service methods this one uses internal client sid
 * NOT web service sid
 *
 * @param sid - internal client sid
 * @param type - the type of document being saved PNG/PDF
 * @param data - binary data
 * @return - serviceResult object with the result
 * @throws {@link ServiceException} in case of any errors
 */
@WebMethod
@POST
@Path("/uploadwb/{type}")
@Operation(description = "This method will receive WB as binary data (png) and store it to temporary PDF/PNG file", responses = { @ApiResponse(responseCode = "200", description = "serviceResult object with the result", content = @Content(schema = @Schema(implementation = ServiceResultWrapper.class))), @ApiResponse(responseCode = "500", description = "Error in case of invalid credentials or server error") })
public ServiceResult uploadWb(@Parameter(required = true, description = "The SID of the User. This SID must be marked as Loggedin") @WebParam(name = "sid") @QueryParam("sid") String sid, @Parameter(required = true, description = "the type of document being saved PNG/PDF") @WebParam(name = "type") @PathParam("type") String type, @Parameter(required = true, description = "binary data") @WebParam(name = "data") @FormParam("data") String data) throws ServiceException {
    log.debug("[uploadwb] type {}", type);
    Client c = cm.getBySid(sid);
    final boolean allowed = c != null && c.getRoom() != null && c.hasRight(Room.Right.MODERATOR) && !c.getRoom().isHidden(RoomElement.ACTION_MENU);
    return performCall(null, sd -> allowed, sd -> {
        try {
            String tDir = System.getProperty("java.io.tmpdir");
            String fuid = randomUUID().toString();
            if (EXTENSION_PDF.equals(type)) {
                try (PDDocument doc = new PDDocument();
                    OutputStream os = new FileOutputStream(Paths.get(tDir, fuid).toFile())) {
                    JSONArray arr = new JSONArray(data);
                    for (int i = 0; i < arr.length(); ++i) {
                        String base64Image = arr.getString(i).split(",")[1];
                        byte[] bb = Base64.decodeBase64(base64Image);
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bb));
                        float width = img.getWidth();
                        float height = img.getHeight();
                        PDPage page = new PDPage(new PDRectangle(width, height));
                        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(doc, img);
                        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page, AppendMode.APPEND, false)) {
                            contentStream.drawImage(pdImageXObject, 0, 0, width, height);
                        }
                        doc.addPage(page);
                    }
                    doc.save(os);
                }
            } else {
                JSONArray arr = new JSONArray(data);
                String base64Image = arr.getString(0).split(",")[1];
                byte[] bb = Base64.decodeBase64(base64Image);
                FileUtils.copyInputStreamToFile(new ByteArrayInputStream(bb), Paths.get(tDir, fuid).toFile());
            }
            return new ServiceResult(fuid, Type.SUCCESS);
        } catch (Exception e) {
            return new ServiceResult(e.getMessage(), Type.ERROR);
        }
    });
}