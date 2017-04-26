/*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package no.simule.actions;

import no.simule.models.Document;
import no.simule.models.cd.*;
import no.simule.utils.QueryUtil;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "modelExplorer")
@SessionScoped
public class ModelExplorerPanel implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient static final Logger logger = Logger.getLogger(ModelExplorerPanel.class);


    private transient TreeNode root;


    public void loadtree(ClassDiagram diagram) {
        root = new DefaultTreeNode(new Document("Files", "-", "Folder"), null);
        for (ClassStructure cs : diagram.getClasses()) {
            TreeNode classname =
                    new DefaultTreeNode("classTop", new Document(cs.getName(), "-", "Folder"), root);


            if (!cs.getAttributes().isEmpty()) {

                TreeNode properties = new DefaultTreeNode("property",
                        new Document("Attributes", "-", "Folder"), classname);
                for (ClassAttribute ca : cs.getAttributes()) {
                    if (!ca.isClass()) {
                        TreeNode propertyname = new DefaultTreeNode("property",
                                new Document(ca.getName(), "-", "Folder"), properties);
                        new DefaultTreeNode("datatype", new Document(ca.getType(), "-", "Folder"),
                                propertyname);
                    }
                }

                TreeNode associations = new DefaultTreeNode("association",
                        new Document("Associations", "-", "Folder"), classname);

                for (ClassAttribute ca : cs.getAttributes()) {
                    if (ca.isClass()) {
                        TreeNode propertyname = new DefaultTreeNode("association",
                                new Document(ca.getName(), "-", "Folder"), associations);
                        new DefaultTreeNode("class", new Document(ca.getType(), "-", "Folder"),
                                propertyname);
                    }
                }

            }


            if (!cs.getOperations().isEmpty()) {

                TreeNode operations = new DefaultTreeNode("operation",
                        new Document("Operations", "-", "Folder"), classname);

                for (ClassOperation cm : cs.getOperations()) {
                    String parameters = QueryUtil.operationPrameters(cm.getParameters());
                    TreeNode propertyname = new DefaultTreeNode("operation",
                            new Document(cm.getName() + parameters, "-", "Folder"), operations);
                    if (cm.getReturnType() != null) {

                        String returnType = QueryUtil.attributeType(cm.getReturnType().getType(), cm.getReturnType().isCollection());
                        new DefaultTreeNode("datatype",
                                new Document(returnType, "-", "Folder"),
                                propertyname);
                    }

                }

            }


            if (!cs.getSuperClasses().isEmpty()) {
                TreeNode superclass = new DefaultTreeNode("superclass",
                        new Document("Superclass", "-", "Folder"), classname);
                for (ClassStructure suuperClass : cs.getSuperClasses()) {
                    new DefaultTreeNode("class", new Document(suuperClass.getName(), "-", "Folder"),
                            superclass);
                }
            }


            if (!cs.getInstances().isEmpty()) {
                TreeNode classInstances = new DefaultTreeNode("instance",
                        new Document("Instances", "-", "Folder"), classname);

                for (ClassInstance instance : cs.getInstances()) {
                    TreeNode classInstance = new DefaultTreeNode("instance",
                            new Document(instance.getName(), "-", "Folder"), classInstances);
                }

            }

        }
    }


    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }


}
