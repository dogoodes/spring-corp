package spring.corp.framework.metadatabean;

import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.metadatabean.types.IType;
import spring.corp.framework.metadatabean.types.mainframe.IgnoreType;

public abstract class AbstractMetaDataManager implements IMetaDataManager {
	
	public IMetaData metaData;

	public AbstractMetaDataManager(IMetaData metaData) {
		this.metaData = metaData;
	}

	/**
	 * Metodo responsavel por transformar a String no objeto MetaData
	 */
	public void materialize(String glueData) {
		for (Iterator<IMetaDataBean> metaDataBeans = metaData.getMetaDataDefinition().getIterator(); metaDataBeans.hasNext();) {
			IMetaDataBean metaDataBean = metaDataBeans.next();
			IType type = metaDataBean.getType();
			int length = type.getLength();
			String[] values = getMaterializedData(glueData, length);
			String data = values[0];
			glueData = values[1];
			if (!(type instanceof IgnoreType)) {
				String name = metaDataBean.getName();
				System.out.println("MetaDataManager: colocando " + data + " para o campo " + name);

				Object newData = type.transform(data);
				try {
					if (metaDataBean instanceof IRuleMetaDataBean) {
						IRuleMetaDataBean rule = (IRuleMetaDataBean) metaDataBean;
						if (rule.isValidSet()) {
							BeanUtils.setProperty(metaData, name, newData);
						}
					} else {
						BeanUtils.setProperty(metaData, name, newData);
					}
				} catch (Exception e) {
					String message = GerenciadorMensagem.getMessage("metadata.datamanager.error.set", new Object[] {newData, name});
					UserException error = new UserException(message, e);
					throw error;
				}
			}
		}
	}

	public abstract String[] getMaterializedData(String glueData, int length);

	/**
	 * Metodo responsavel por transformar o objeto MetaData em String
	 */
	public String deMaterialize() {
		StringBuilder glueData = new StringBuilder();
		for (Iterator<IMetaDataBean> metaDataBeans = metaData.getMetaDataDefinition().getIterator(); metaDataBeans.hasNext();) {
			IMetaDataBean metaDataBean = metaDataBeans.next();
			String strData = getDeMaterializedData(metaDataBean);
			glueData.append(strData);
		}
		// System.out.println(logInfo.toString());
		return glueData.toString();
	}
	
	public String getDeMaterializedData(IMetaDataBean metaDataBean){
		PropertyUtilsBean beanUtils = BeanUtilsBean.getInstance().getPropertyUtils();
		StringBuilder logInfo = new StringBuilder();
		IType type = metaDataBean.getType();
		String name = metaDataBean.getName();
		String strData = null;
		try {
			Object data = null;
			if (!(type instanceof IgnoreType)) {
				if (metaDataBean instanceof IRuleMetaDataBean) {
					IRuleMetaDataBean rule = (IRuleMetaDataBean) metaDataBean;
					if (rule.isValiGet()) {
						data = beanUtils.getProperty(metaData, name);
						logInfo.append("name:" + name + " value: " + data + "\n");
					}
				} else {
					data = beanUtils.getProperty(metaData, name);
					logInfo.append("name:" + name + " value: " + data + "\n");
				}
			} else {
				logInfo.append("name:" + name + " value: IgnoreType \n");
			}
			strData = getDeMaterializeData(type, data);
		} catch (Exception e) {
			String message = GerenciadorMensagem.getMessage("metadata.datamanager.error.get", name);
			UserException error = new UserException(message, e);
			throw error;
		}
		return strData;
	}

	public abstract String getDeMaterializeData(IType type, Object data);
}