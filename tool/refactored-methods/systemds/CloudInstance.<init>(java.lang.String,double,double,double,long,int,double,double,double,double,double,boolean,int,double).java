public CloudInstance(String instanceName, double pricePerHour, double extraFee, double storagePrice, long memory, int vCPUCores, double GFlops, double memoryBandwidth, double diskReadBandwidth, double diskWriteBandwidth, double networkBandwidth, boolean NVMeStorage, int numberStorageVolumes, double sizeStorageVolumes) {
    this.instanceName = instanceName;
    // instance price per second
    this.instancePrice = pricePerHour / 3600;
    // extra fee per second
    this.extraFee = extraFee / 3600;
    if (NVMeStorage) {
        // no need of attaching extra
        this.extraStoragePrice = 0;
    } else {
        this.extraStoragePrice = storagePrice * (EBS_DEFAULT_ROOT_SIZE_EMR + numberStorageVolumes * sizeStorageVolumes) / // total storage price per second
        (30 * 24 * 3600);
    }
    this.memory = memory;
    this.vCPUCores = vCPUCores;
    this.flops = (long) (GFlops * 1024) * 1024 * 1024;
    this.memoryBandwidth = memoryBandwidth;
    this.diskReadBandwidth = diskReadBandwidth;
    this.diskWriteBandwidth = diskWriteBandwidth;
    this.networkBandwidth = networkBandwidth;
    this.NVMeStorage = NVMeStorage;
    this.numberStorageVolumes = numberStorageVolumes;
    this.sizeStorageVolumes = sizeStorageVolumes;
}