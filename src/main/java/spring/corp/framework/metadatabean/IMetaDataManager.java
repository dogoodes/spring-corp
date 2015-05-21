package spring.corp.framework.metadatabean;

public interface IMetaDataManager {

	public String deMaterialize();
	public void materialize(String glueData);
	public IMetaDataManager newInstance(IMetaData metaData);
}