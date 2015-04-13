package br.com.unb.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.labels.AccountLabel;
import br.com.unb.labels.ActivityLabel;
import br.com.unb.labels.CollectionLabel;
import br.com.unb.labels.ProjectLabel;
import br.com.unb.model.EntityType;
import br.com.unb.model.Project;
import br.com.unb.model.RelationshipProvenanceType;
import br.com.unb.model.User;
import br.com.unb.transform.ProjectTransform;
import br.com.unb.util.DateUtil;

@Component
public class ProjectRepository {

	@Inject private GraphDatabaseService graphDb;
	@Inject private ProjectTransform<Project> projectTransform;
	
	public ProjectRepository(GraphDatabaseService graphDb, ProjectTransform<Project> transform) {
		this.graphDb = graphDb;
		this.projectTransform = transform;
	}
	
	public List<Project> list(User user) {
		List<Project> projects = new ArrayList<>();
		Node root = graphDb.getNodeById(user.getId());
		Iterable<Relationship> itRelHas= root.getRelationships(RelationshipProvenanceType.HAS);
		if (itRelHas != null) {
			for (Relationship relHas : itRelHas) {
				Node possibleNodeProject = relHas.getEndNode();
				if (EntityType.PROJECT.getName().equals(possibleNodeProject.getProperty("type"))) {
					projects.add(projectTransform.transform2Entity(possibleNodeProject));
				}
			}
		}
		
		return projects;
	}
	
	public void save(Project project) {
		Node nodeUser = graphDb.getNodeById(project.getUsuario().getId());
		Node nodeProject = projectTransform.transform2Node(project, graphDb.createNode());
		
		Relationship relationshipHas = nodeUser.createRelationshipTo(nodeProject, RelationshipProvenanceType.HAS);
		relationshipHas.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
	}
	
	public Project findProjectByAccount(long idAccount) {
		Node nodeAccount = graphDb.getNodeById(idAccount);
		Relationship relHas = nodeAccount.getSingleRelationship(RelationshipProvenanceType.HAS, Direction.INCOMING);
		return projectTransform.transform2Entity(relHas.getStartNode());
	}

	public Project findById(Long idProject) {
		Node nodeProject = graphDb.getNodeById(idProject);
		return projectTransform.transform2Entity(nodeProject);
	}
	
	private Node createNode() {
		return this.graphDb.createNode();
	}
	
	public void createTestGraph(Long idUser) {
		Node nodeUser = graphDb.getNodeById(idUser);
		
		Node nodeProject = createNode();
		nodeProject.setProperty(ProjectLabel.NAME.getLabel(), "PROJECT_TESTE");
		nodeProject.setProperty(ProjectLabel.DESCRIPTION.getLabel(), "PROJECT_BACILLUS");
		nodeProject.setProperty(ProjectLabel.COORDENATOR.getLabel(), "MARISTELA T. HOLANDA");
		nodeProject.setProperty(ProjectLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeProject.setProperty(ProjectLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeProject.setProperty(ProjectLabel.OBSERVATION.getLabel(), "PROJECT TESTE");
		nodeProject.setProperty(ProjectLabel.TYPE.getLabel(), EntityType.PROJECT.getName());
		Relationship userHasProject = nodeUser.createRelationshipTo(nodeProject, RelationshipProvenanceType.HAS);
		userHasProject.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());
		
		Node nodeAccount = createNode();
		nodeAccount.setProperty(AccountLabel.NAME.getLabel(), "ACCOUNT_TESTE");
		nodeAccount.setProperty(AccountLabel.DESCRIPTION.getLabel(), "ACCOUNT TESTE");
		nodeAccount.setProperty(AccountLabel.EXECUTION_PLACE.getLabel(), "UNB");
		nodeAccount.setProperty(AccountLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeAccount.setProperty(AccountLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeAccount.setProperty(AccountLabel.OBSERVATION.getLabel(), "ACCOUNT TESTE");
		nodeAccount.setProperty(AccountLabel.VERSION.getLabel(), "1");
		nodeAccount.setProperty(AccountLabel.VERSION_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeAccount.setProperty("type", EntityType.ACCOUNT.getName());
		Relationship projectHasAccount = nodeProject.createRelationshipTo(nodeAccount, RelationshipProvenanceType.HAS);
		projectHasAccount.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());
		
		Node activity01 = createNode();
		activity01.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "python");
		activity01.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "2.4.1");
		activity01.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "python copyfile in.txt out.txt");
		activity01.setProperty(ActivityLabel.FUNCTION.getLabel(), "Copiar");
		activity01.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity01.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity01.setProperty(ActivityLabel.NAME.getLabel(), "A001_COPIAR");
		activity01.setProperty(ActivityLabel.FILE_NAME.getLabel(), "in");
		activity01.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity01.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity01.setProperty(ActivityLabel.END_HOUR.getLabel(), "11:00");
		Relationship accountHasActivity01 = nodeAccount.createRelationshipTo(activity01, RelationshipProvenanceType.HAS);
		accountHasActivity01.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity01WasAssociatedWith = activity01.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity01WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		
		Node c001 = createNode();
		c001.setProperty(CollectionLabel.NAME.getLabel(), "in.txt");
		c001.setProperty(CollectionLabel.SIZE.getLabel(), 13);
		c001.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/in.txt");
		c001.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c001Used = activity01.createRelationshipTo(c001, RelationshipProvenanceType.USED);
		c001Used.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		
		Node c002 = createNode();
		c002.setProperty(CollectionLabel.NAME.getLabel(), "out.txt");
		c002.setProperty(CollectionLabel.SIZE.getLabel(), 13);
		c002.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/out/out.txt");
		c002.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c007WasGenerated = c002.createRelationshipTo(activity01, RelationshipProvenanceType.WAS_GENERATED_BY);
		c007WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c007WasDerivedC005 = c002.createRelationshipTo(c001, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c007WasDerivedC005.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
	}
	
	public void createGraph(Long idUser) {
		Node nodeUser = graphDb.getNodeById(idUser);
		
		Node nodeProject = createNode();
		nodeProject.setProperty(ProjectLabel.NAME.getLabel(), "PROJECT_BACILLUS");
		nodeProject.setProperty(ProjectLabel.DESCRIPTION.getLabel(), "PROJECT_BACILLUS");
		nodeProject.setProperty(ProjectLabel.COORDENATOR.getLabel(), "MARISTELA T. HOLANDA");
		nodeProject.setProperty(ProjectLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeProject.setProperty(ProjectLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeProject.setProperty(ProjectLabel.OBSERVATION.getLabel(), "PROJECT BACILLUS");
		nodeProject.setProperty(ProjectLabel.TYPE.getLabel(), EntityType.PROJECT.getName());
//		nodeProject.setProperty("usuario", "AG_RODRIGO");
		Relationship userHasProject = nodeUser.createRelationshipTo(nodeProject, RelationshipProvenanceType.HAS);
		userHasProject.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());
		
		Node nodeAccount = createNode();
		nodeAccount.setProperty(AccountLabel.NAME.getLabel(), "ACCOUNT_BACILLUS");
		nodeAccount.setProperty(AccountLabel.DESCRIPTION.getLabel(), "ACCOUNT BACILLUS");
		nodeAccount.setProperty(AccountLabel.EXECUTION_PLACE.getLabel(), "UNB");
		nodeAccount.setProperty(AccountLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeAccount.setProperty(AccountLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeAccount.setProperty(AccountLabel.OBSERVATION.getLabel(), "ACCOUNT BACILLUS");
		nodeAccount.setProperty(AccountLabel.VERSION.getLabel(), "1");
		nodeAccount.setProperty(AccountLabel.VERSION_DATE.getLabel(), DateUtil.date2String(new Date()));
		nodeAccount.setProperty("type", EntityType.ACCOUNT.getName());
		Relationship projectHasAccount = nodeProject.createRelationshipTo(nodeAccount, RelationshipProvenanceType.HAS);
		projectHasAccount.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());
		
		Node activity01 = createNode();
		activity01.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "perl");
		activity01.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "5.4.1");
		activity01.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "perl");
		activity01.setProperty(ActivityLabel.FUNCTION.getLabel(), "Filtrar");
		activity01.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity01.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity01.setProperty(ActivityLabel.NAME.getLabel(), "A001_Family13_Filtro");
		activity01.setProperty(ActivityLabel.FILE_NAME.getLabel(), "C001_Family13");
		activity01.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity01.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity01.setProperty(ActivityLabel.END_HOUR.getLabel(), "11:00");
		Relationship accountHasActivity01 = nodeAccount.createRelationshipTo(activity01, RelationshipProvenanceType.HAS);
		accountHasActivity01.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity01WasAssociatedWith = activity01.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity01WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		
		Node c001 = createNode();
		c001.setProperty(CollectionLabel.NAME.getLabel(), "C001_Family13");
		c001.setProperty(CollectionLabel.SIZE.getLabel(), 13);
		c001.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C001_Family13");
		c001.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c001Used = activity01.createRelationshipTo(c001, RelationshipProvenanceType.USED);
		c001Used.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		
		Node activity02 = createNode();
		activity02.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "perl");
		activity02.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "5.4.1");
		activity02.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "perl");
		activity02.setProperty(ActivityLabel.FUNCTION.getLabel(), "Filtrar");
		activity02.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity02.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity02.setProperty(ActivityLabel.NAME.getLabel(), "A002_Family57_Filtro");
		activity02.setProperty(ActivityLabel.FILE_NAME.getLabel(), "C002_Family57");
		activity02.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity02.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity02.setProperty(ActivityLabel.END_DATE.getLabel(), "11:00");
		Relationship accountHasActivity02 = nodeAccount.createRelationshipTo(activity02, RelationshipProvenanceType.HAS);
		accountHasActivity02.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity02WasAssociatedWith = activity02.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity02WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		
		Node c002 = createNode();
		c002.setProperty(CollectionLabel.NAME.getLabel(), "C002_Family57");
		c002.setProperty(CollectionLabel.SIZE.getLabel(), 13);
		c002.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C002_Family57");
		c002.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c002Used = activity02.createRelationshipTo(c002, RelationshipProvenanceType.USED);
		c002Used.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		
		Node c003 = createNode();
		c003.setProperty(CollectionLabel.NAME.getLabel(), "C003_Family13_Filtrado");
		c003.setProperty(CollectionLabel.SIZE.getLabel(), 13);
		c003.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C003_Family13_Filtrado");
		c003.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c003WasGenerated = c003.createRelationshipTo(activity01, RelationshipProvenanceType.WAS_GENERATED_BY);
		c003WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c003WasDerived = c003.createRelationshipTo(c001, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c003WasDerived.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		
		Node c004 = createNode();
		c004.setProperty(CollectionLabel.NAME.getLabel(), "C004_Family57_Filtrado");
		c004.setProperty(CollectionLabel.SIZE.getLabel(), 13);
		c004.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C004_Family57_Filtrado");
		c004.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c004WasGenerated = c004.createRelationshipTo(activity02, RelationshipProvenanceType.WAS_GENERATED_BY);
		c004WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c004WasDerived = c004.createRelationshipTo(c002, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c004WasDerived.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		
		Node c005 = createNode();
		c005.setProperty(CollectionLabel.NAME.getLabel(), "C005_ORFS");
		c005.setProperty(CollectionLabel.SIZE.getLabel(), 13);
		c005.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C005_ORFS");
		c005.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		
		Node activity03 = createNode();
		activity03.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "perl");
		activity03.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "5.4.1");
		activity03.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "perl");
		activity03.setProperty(ActivityLabel.FUNCTION.getLabel(), "Filtrar");
		activity03.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity03.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity03.setProperty(ActivityLabel.NAME.getLabel(), "A003_Family13_Merge");
		activity03.setProperty(ActivityLabel.FILE_NAME.getLabel(), "C003_Family13_Filtrado, C005_ORFS");
		activity03.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity03.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity03.setProperty(ActivityLabel.END_HOUR.getLabel(), "11:00");
		Relationship accountHasActivity03 = nodeAccount.createRelationshipTo(activity03, RelationshipProvenanceType.HAS);
		accountHasActivity03.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity03WasAssociatedWith = activity03.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity03WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		Relationship activity03UsedC005 = activity03.createRelationshipTo(c005, RelationshipProvenanceType.USED);
		activity03UsedC005.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		Relationship activity03UsedC003 = activity03.createRelationshipTo(c003, RelationshipProvenanceType.USED);
		activity03UsedC003.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		
		Node activity04 = createNode();
		activity04.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "perl");
		activity04.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "5.4.1");
		activity04.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "perl");
		activity04.setProperty(ActivityLabel.FUNCTION.getLabel(), "Filtrar");
		activity04.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity04.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity04.setProperty(ActivityLabel.NAME.getLabel(), "A004_Family57_Merge");
		activity04.setProperty(ActivityLabel.FILE_NAME.getLabel(), "C004_Family57_Filtrado, C005_ORFS");
		activity04.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity04.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity04.setProperty(ActivityLabel.END_HOUR.getLabel(), "12:00");
		Relationship accountHasActivity04 = nodeAccount.createRelationshipTo(activity04, RelationshipProvenanceType.HAS);
		accountHasActivity04.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity04WasAssociatedWith = activity04.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity04WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		Relationship activity04UsedC005 = activity04.createRelationshipTo(c005, RelationshipProvenanceType.USED);
		activity04UsedC005.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		Relationship activity04UsedC004 = activity04.createRelationshipTo(c004, RelationshipProvenanceType.USED);
		activity04UsedC004.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());

		Node c006 = createNode();
		c006.setProperty(CollectionLabel.NAME.getLabel(), "C006_Family13_ORFS");
		c006.setProperty(CollectionLabel.SIZE.getLabel(), 26);
		c006.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C006_Family13_ORFS");
		c006.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c006WasGenerated = c006.createRelationshipTo(activity03, RelationshipProvenanceType.WAS_GENERATED_BY);
		c006WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c006WasDerivedC005 = c006.createRelationshipTo(c005, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c006WasDerivedC005.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		Relationship c006WasDerivedC003 = c006.createRelationshipTo(c003, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c006WasDerivedC003.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		
		Node c007 = createNode();
		c007.setProperty(CollectionLabel.NAME.getLabel(), "C007_Family57_ORFS");
		c007.setProperty(CollectionLabel.SIZE.getLabel(), 26);
		c007.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C007_Family57_ORFS");
		c007.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c007WasGenerated = c007.createRelationshipTo(activity04, RelationshipProvenanceType.WAS_GENERATED_BY);
		c007WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c007WasDerivedC005 = c007.createRelationshipTo(c005, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c007WasDerivedC005.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		Relationship c007WasDerivedC004 = c007.createRelationshipTo(c004, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c007WasDerivedC004.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		
		Node activity05 = createNode();
		activity05.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "perl");
		activity05.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "5.4.1");
		activity05.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "perl");
		activity05.setProperty(ActivityLabel.FUNCTION.getLabel(), "Filtrar");
		activity05.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity05.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity05.setProperty(ActivityLabel.NAME.getLabel(), "A005_Family13_Alignment");
		activity05.setProperty(ActivityLabel.FILE_NAME.getLabel(), "C006_Family13_ORFS");
		activity05.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity05.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity05.setProperty(ActivityLabel.END_HOUR.getLabel(), "10:00");
		Relationship accountHasActivity05 = nodeAccount.createRelationshipTo(activity05, RelationshipProvenanceType.HAS);
		accountHasActivity05.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity05WasAssociatedWith = activity05.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity05WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		Relationship activity05UsedC006 = activity05.createRelationshipTo(c006, RelationshipProvenanceType.USED);
		activity05UsedC006.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		
		Node activity06 = createNode();
		activity06.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "perl");
		activity06.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "5.4.1");
		activity06.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "perl");
		activity06.setProperty(ActivityLabel.FUNCTION.getLabel(), "Filtrar");
		activity06.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity06.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity06.setProperty(ActivityLabel.NAME.getLabel(), "A006_Family57_Alignment");
		activity06.setProperty(ActivityLabel.FILE_NAME.getLabel(), "C007_Family57_ORFS");
		activity06.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity06.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity06.setProperty(ActivityLabel.END_HOUR.getLabel(), "12:00");
		Relationship accountHasActivity06 = nodeAccount.createRelationshipTo(activity06, RelationshipProvenanceType.HAS);
		accountHasActivity06.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity06WasAssociatedWith = activity06.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity06WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		Relationship activity06UsedC007 = activity06.createRelationshipTo(c007, RelationshipProvenanceType.USED);
		activity06UsedC007.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		
		Node c008 = createNode();
		c008.setProperty(CollectionLabel.NAME.getLabel(), "C008_Family13_Alignment");
		c008.setProperty(CollectionLabel.SIZE.getLabel(), 26);
		c008.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C008_Family13_Alignment");
		c008.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c008WasGenerated = c008.createRelationshipTo(activity05, RelationshipProvenanceType.WAS_GENERATED_BY);
		c008WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c008WasDerivedC006 = c008.createRelationshipTo(c006, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c008WasDerivedC006.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		
		Node c009 = createNode();
		c009.setProperty(CollectionLabel.NAME.getLabel(), "C009_Family57_Alignment");
		c009.setProperty(CollectionLabel.SIZE.getLabel(), 26);
		c009.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C009_Family57_Alignment");
		c009.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c009WasGenerated = c009.createRelationshipTo(activity06, RelationshipProvenanceType.WAS_GENERATED_BY);
		c009WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c009WasDerivedC007 = c009.createRelationshipTo(c007, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c009WasDerivedC007.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		
		Node activity07 = createNode();
		activity07.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "perl");
		activity07.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "5.4.1");
		activity07.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "perl");
		activity07.setProperty(ActivityLabel.FUNCTION.getLabel(), "Filtrar");
		activity07.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity07.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity07.setProperty(ActivityLabel.NAME.getLabel(), "A007_Family13_Graph");
		activity07.setProperty(ActivityLabel.FILE_NAME.getLabel(), "C008_Family13_Alignment");
		activity07.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity07.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity07.setProperty(ActivityLabel.END_HOUR.getLabel(), "11:00");
		Relationship accountHasActivity07 = nodeAccount.createRelationshipTo(activity07, RelationshipProvenanceType.HAS);
		accountHasActivity07.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity07WasAssociatedWith = activity07.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity07WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		Relationship activity07UsedC008 = activity07.createRelationshipTo(c008, RelationshipProvenanceType.USED);
		activity07UsedC008.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		
		Node activity08 = createNode();
		activity08.setProperty(ActivityLabel.PROGRAM_NAME.getLabel(), "perl");
		activity08.setProperty(ActivityLabel.PROGRAM_VERSION.getLabel(), "5.4.1");
		activity08.setProperty(ActivityLabel.COMMAND_LINE.getLabel(), "perl");
		activity08.setProperty(ActivityLabel.FUNCTION.getLabel(), "Filtrar");
		activity08.setProperty(ActivityLabel.START_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity08.setProperty(ActivityLabel.END_DATE.getLabel(), DateUtil.date2String(new Date()));
		activity08.setProperty(ActivityLabel.NAME.getLabel(), "A008_Family57_Graph");
		activity08.setProperty(ActivityLabel.FILE_NAME.getLabel(), "C008_Family57_Alignment");
		activity08.setProperty(ActivityLabel.TYPE.getLabel(), EntityType.ACTIVITY.getName());
		activity08.setProperty(ActivityLabel.START_HOUR.getLabel(), "10:00");
		activity08.setProperty(ActivityLabel.END_HOUR.getLabel(), "12:00");
		Relationship accountHasActivity08 = nodeAccount.createRelationshipTo(activity08, RelationshipProvenanceType.HAS);
		accountHasActivity08.setProperty("relationship-type", RelationshipProvenanceType.HAS.getName());;
		Relationship activity08WasAssociatedWith = activity08.createRelationshipTo(nodeUser, RelationshipProvenanceType.WAS_ASSOCIATED_WITH);
		activity08WasAssociatedWith.setProperty("relationship-type", RelationshipProvenanceType.WAS_ASSOCIATED_WITH.getName());
		Relationship activity08UsedC009 = activity08.createRelationshipTo(c009, RelationshipProvenanceType.USED);
		activity08UsedC009.setProperty("relationship-type", RelationshipProvenanceType.USED.getName());
		
		Node c010 = createNode();
		c010.setProperty(CollectionLabel.NAME.getLabel(), "C010_Family13_Graph");
		c010.setProperty(CollectionLabel.SIZE.getLabel(), 26);
		c010.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C010_Family13_Graph");
		c010.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c010WasGenerated = c010.createRelationshipTo(activity07, RelationshipProvenanceType.WAS_GENERATED_BY);
		c010WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c010WasDerivedC008 = c010.createRelationshipTo(c008, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c010WasDerivedC008.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
		
		Node c011 = createNode();
		c011.setProperty(CollectionLabel.NAME.getLabel(), "C011_Family57_Graph");
		c011.setProperty(CollectionLabel.SIZE.getLabel(), 26);
		c011.setProperty(CollectionLabel.LOCATION.getLabel(), "Users/Documents/ProvenanceFiles/C011_Family57_Graph");
		c011.setProperty(CollectionLabel.TYPE.getLabel(), EntityType.COLLECTION.getName());
		Relationship c011WasGenerated = c011.createRelationshipTo(activity08, RelationshipProvenanceType.WAS_GENERATED_BY);
		c011WasGenerated.setProperty("relationship-type", RelationshipProvenanceType.WAS_GENERATED_BY.getName());
		Relationship c011WasDerivedC009 = c011.createRelationshipTo(c009, RelationshipProvenanceType.WAS_DERIVED_FROM);
		c011WasDerivedC009.setProperty("relationship-type", RelationshipProvenanceType.WAS_DERIVED_FROM.getName());
	}

}
