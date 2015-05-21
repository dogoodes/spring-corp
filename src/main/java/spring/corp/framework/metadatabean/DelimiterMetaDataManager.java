package spring.corp.framework.metadatabean;

import spring.corp.framework.metadatabean.types.IType;

public class DelimiterMetaDataManager extends AbstractMetaDataManager {
	
	private char delimiter;

	public DelimiterMetaDataManager(IMetaData metaData, char delimiter) {
		super(metaData);
		this.delimiter = delimiter;
	}
	
	public DelimiterMetaDataManager newInstance(IMetaData metaData){
		return new DelimiterMetaDataManager(metaData, this.delimiter);
	}

	@Override
	public String[] getMaterializedData(String glueData, int length) {
		String[] ret = new String[]{"", ""};
		if (length == 0) {
			length = glueData.indexOf(delimiter);
		}
		ret[0] = glueData.substring(0, length);
		ret[1] = glueData.substring(length+1);
		return ret;
	}

	@Override
	public String getDeMaterializeData(IType type, Object data) {
		String dematerialized = null;
		if (type instanceof OccursMetaDataBean) {
			dematerialized = (String) type.transform(data);
		} else {
			dematerialized = (String) type.transform(data) + delimiter;
		}
		return dematerialized;
	}
}