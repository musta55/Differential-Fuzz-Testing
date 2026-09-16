private void update(Whiteboards wbs) {
    onlineWbs.put(wbs.getRoomId(), wbs);
    new Thread(() -> map().put(wbs.getRoomId(), wbs)).start();
}