package spring.corp.framework.metadatabean;

import java.util.ArrayList;
import java.util.List;

import spring.corp.framework.metadatabean.rules.IRule;
import spring.corp.framework.metadatabean.types.IType;

public class RuleMetaDataBean extends MetaDataBean implements IRuleMetaDataBean {

    private List<IRule> setRules = new ArrayList<IRule>();
    private List<IRule> getRules = new ArrayList<IRule>();
    private List<IRule> useRules = new ArrayList<IRule>();

    public RuleMetaDataBean(String name, IType type) {
        super(name, type);
    }

    public void addGetRule(IRule rule) {
        getRules.add(rule);
    }

    public void addSetRule(IRule rule) {
        setRules.add(rule);
    }
    
    public void addUseRule(IRule rule){
        useRules.add(rule);
    }
    
    public boolean isValidUse() {
        boolean isValid = true;
        for (IRule rule: useRules) {
            isValid = rule.isValid();
            if (!isValid) {
            	break;
            }
        }
        return isValid;
    }

    public boolean isValidSet() {
        boolean isValid = true;
        for (IRule rule: setRules) {
            isValid = rule.isValid();
            if (!isValid) {
            	break;
            }
        }
        return isValid;
    }

    public boolean isValiGet() {
        boolean isValid = true;
        for (IRule rule: getRules) {
            isValid = rule.isValid();
            if (!isValid) {
            	break;
            }
        }
        return isValid;
    }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public IMetaDataBean deepCopy() throws CloneNotSupportedException{
		return (IMetaDataBean)clone();
	}
}