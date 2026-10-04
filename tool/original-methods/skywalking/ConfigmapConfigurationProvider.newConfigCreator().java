@Override
public ConfigCreator newConfigCreator() {
    return new ConfigCreator<ConfigmapConfigurationSettings>() {

        @Override
        public Class type() {
            return ConfigmapConfigurationSettings.class;
        }

        @Override
        public void onInitialized(final ConfigmapConfigurationSettings initialized) {
            settings = initialized;
        }
    };
}