public static String displayStatistics() {
    if (optCount.longValue() > 0) {
        StringBuilder sb = new StringBuilder();
        sb.append("ParFor loops optimized:\t\t" + getOptCount() + ".\n");
        sb.append("ParFor optimize time:\t\t" + String.format("%.3f", ((double) getOptTime()) / 1000) + " sec.\n");
        sb.append("ParFor initialize time:\t\t" + String.format("%.3f", ((double) getInitTime()) / 1000) + " sec.\n");
        sb.append("ParFor result merge time:\t" + String.format("%.3f", ((double) getMergeTime()) / 1000) + " sec.\n");
        sb.append("ParFor total update in-place:\t" + Statistics.getTotalUIPVar() + "/" + Statistics.getTotalLixUIP() + "/" + Statistics.getTotalLix() + "\n");
        return sb.toString();
    }
    return "";
}