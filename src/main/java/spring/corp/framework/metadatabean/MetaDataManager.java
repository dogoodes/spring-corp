package spring.corp.framework.metadatabean;

import spring.corp.framework.metadatabean.types.IType;

public class MetaDataManager extends AbstractMetaDataManager {

	public MetaDataManager(IMetaData metaData) {
		super(metaData);
	}
	
	public MetaDataManager newInstance(IMetaData metaData){
		return new MetaDataManager(metaData);
	}

	@Override
	public String[] getMaterializedData(String glueData, int length) {
		String[] ret = new String[]{"", ""};
		ret[0] = glueData.substring(0, length);
		ret[1] = glueData.substring(length);
		return ret;
	}

	@Override
	public String getDeMaterializeData(IType type, Object data) {
		return (String) type.transform(data);
	}
}