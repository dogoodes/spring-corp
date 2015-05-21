package spring.corp.framework.metadatabean;

import java.util.ArrayList;
import java.util.List;

import spring.corp.framework.metadatabean.types.IType;

public class DecisionMetaDataBean implements IDecisionMetaDataBean {

    private List<IRuleMetaDataBean> rules = new ArrayList<IRuleMetaDataBean>();

    public void addRule(IRuleMetaDataBean rule) {
        rules.add(rule);
    }

    public String getName() {
        return choice().getName();
    }

    public IType getType() {
        return choice().getType();
    }

    private IMetaDataBean choice() {
        for (IRuleMetaDataBean rule : rules) {
            if (rule.isValidUse()) {
                IMetaDataBean metaData = (IMetaDataBean) rule;
                return metaData;
            }
        }
        return null;
    }

	@Override
	public IMetaDataBean deepCopy() throws CloneNotSupportedException {
		throw new IllegalStateException("Method not implemented");
	}
}