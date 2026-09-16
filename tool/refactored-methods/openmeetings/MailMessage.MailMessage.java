public MailMessage(String recipients, String replyTo, String subject, String body, IcalHandler ical) {
    this(recipients, replyTo, subject, body);
    if (ical != null) {
        this.icsMethod = ical.getMethod().getValue();
        try {
            this.ics = ical.toByteArray();
        } catch (ValidationException | IOException e) {
            log.error("Unexpected error while getting ICS", e);
        }
    }
}