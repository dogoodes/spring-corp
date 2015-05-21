package spring.corp.framework.metadatabean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.metadatabean.types.ConstructorType;
import spring.corp.framework.metadatabean.types.mainframe.AbstractType;

public class OccursMetaDataBean extends AbstractType implements IOccursType, IMetaData {

    private IMetaDataDefinition definition = new MetaDataDefinition();
    private IMetaDataDefinition occurMetaDataDefinition = null;
    private List<IMetaDataDefinition> occurs = new LinkedList<IMetaDataDefinition>();
    private ConstructorType constructorType;
    private Object implementationType;
    private int size = 0;
    private IMetaDataManager metaDataManager;
   
    public OccursMetaDataBean(ConstructorType constructorType) {
        super(0);
        this.constructorType = constructorType;
    }
    
    public OccursMetaDataBean(ConstructorType constructorType, IMetaDataManager metaDataManager) {
        super(0);
        this.constructorType = constructorType;
        this.metaDataManager = metaDataManager;
    }
    
    public int size() {
    	return size;
    }
   
    /**
     * Metodo responsavel por adicionar um metadado sobre o dado contido nas
     * ocorrencias, os dados terao em seus nomes a inclusao do sufixo
     * implementationType pois o MetaDataManager sera acionado para fazer a
     * deMaterializacao/materializacao deste bean.
     * @see br.com.aymore.web.afc.service.metadatabean.IOccursType#addMetaDataBean(br.com.aymore.web.afc.service.metadatabean.IMetaDataBean)
     */
    public void addMetaDataBean(IMetaDataBean metaDataBean) {
        addMetaDataBean(metaDataBean, true);
    }
    
    protected void addMetaDataBean(IMetaDataBean metaDataBean, boolean addSufixo) {
    	String name = null;
    	if (addSufixo) {
    		name = "implementationType." + metaDataBean.getName();
    	} else {
    		name = metaDataBean.getName();
    	}
        IMetaDataBean occursMetaDataBean = new MetaDataBean(name, metaDataBean.getType());
        definition.addMetaDataBean(occursMetaDataBean);
        if (size == 0) {
        	length += metaDataBean.getType().getLength();
        }
    }
    
    public void flush(int _size) {
    	 if (constructorType == null) {
             String message = GerenciadorMensagem.getMessage("metadata.implementation.occurs.not.specified");
             throw new IllegalStateException(message);
         }
    	 List<IMetaDataBean>  copiesMetaDataBean = new LinkedList<IMetaDataBean>();
    	 for (Iterator<IMetaDataBean> it = definition.getIterator(); it.hasNext();) {
	 			IMetaDataBean imeta = it.next();
	 			try {
	 				copiesMetaDataBean.add(imeta.deepCopy());
	 			} catch (CloneNotSupportedException e) {
	 				e.printStackTrace();
	 			}
    	 }
    	 flush();
    	 _size--;
	     while (_size > 0) {
	    	 for (Iterator<IMetaDataBean> it = copiesMetaDataBean.iterator(); it.hasNext();) {
	    		 try {
	    			 addMetaDataBean(it.next().deepCopy(), false);
	    		 } catch (CloneNotSupportedException e) {
	 				e.printStackTrace();
	 			}
	    	 }
	    	 flush();
	    	 _size--;
	     }
    }
    
    public void flush() {
        if (constructorType == null) {
            String message = GerenciadorMensagem.getMessage("metadata.implementation.occurs.not.specified");
            throw new IllegalStateException(message);
        }
        if (occurMetaDataDefinition == null) {
        	occurMetaDataDefinition = definition;
        }
        occurs.add(definition);
        size++;
        definition = new MetaDataDefinition();
    }

    protected IMetaDataManager newMetaDataManager(IMetaData metaData) {
    	if (metaDataManager == null) {
    		return new MetaDataManager(metaData);
    	} else {
    		return metaDataManager.newInstance(metaData);
    	}
    }
    
    /**
     * Metodo responsavel por deMaterializar o dado contido dentro da lista.
     * Como estamos utilizando a classe MetaDataManager para fazer a
     * deMaterializacao foi necessario criar o atributo implementationType pois
     * guarda o objeto que esta sendo percorrido na lista.
     */
    private String deMaterialize(Collection value) {
    	if (getOccurMetaDataDefinition() == null) {
    		flush(value.size());
    	}
        this.definition = getOccurMetaDataDefinition();
        StringBuilder glueData = new StringBuilder();
        IMetaDataManager metaDataManager = newMetaDataManager(this);
        for (Iterator it = value.iterator(); it.hasNext();) {
            Object o = it.next();
            this.implementationType = o;
            String data = metaDataManager.deMaterialize();
            glueData.append(data);
        }
        String valueStr = glueData.toString();
        valueStr = TransformerUtils.toString(this, valueStr);
        return valueStr;
    }

    private List materialize(String value) {
        List datas = new ArrayList();
        if (value != null && !value.trim().equals("")) {
            IMetaDataManager metaDataManager = newMetaDataManager(this);
            for (IMetaDataDefinition definition : occurs) {
                this.implementationType = constructorType.toObject();
                this.definition = definition;
                metaDataManager.materialize(value);
                datas.add(this.getImplementationType());
                value = value.substring(this.length);
            }
        }
        return datas;
    }

    public Object transform(Object value) {
        try {
            this.valueIn = value;
            if (value instanceof Collection) {
                if (((Collection) value).isEmpty()) {
                    this.valueOut = super.transform(null);
                } else {
                    this.valueOut = deMaterialize((Collection) value);
                }
            } else if (value instanceof String) {
                this.valueOut = materialize((String) value);
            } else {
                this.valueOut = super.transform(value);
            }
            return valueOut;
        } finally {
            clean();
        }
    }

    @Override
    public Object transform() {
        return null;
    }
    
    public IMetaDataDefinition getOccurMetaDataDefinition(){
    	return occurMetaDataDefinition;
    }

    /**
     * Retorna o tamanho da String para todas as ocorrencias da Lista
     * @see br.com.aymore.web.afc.service.metadatabean.types.IType#getLength()
     */
    public int getLength() {
        return length * size;
    }

    protected void clean() {
        this.definition = null;
    }

    public Object getImplementationType() {
        return implementationType;
    }

    public void setImplementationType(Object implementationType) {
        this.implementationType = implementationType;
    }

    public IMetaDataDefinition getMetaDataDefinition() {
        return definition;
    }
}