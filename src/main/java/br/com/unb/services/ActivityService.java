package br.com.unb.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.infra.SessionUser;
import br.com.unb.model.Account;
import br.com.unb.model.Activity;
import br.com.unb.model.CollectionProvenance;
import br.com.unb.repository.ActivityRepository;
import br.com.unb.util.FileUtil;

@Component
public class ActivityService {

	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String[] END_NAMES_FILE = new String[]{".txt",".py", ".java"};
	
	@Inject private ActivityRepository activityRepository;
	@Inject private AccountService accountService;
	@Inject private GroupService groupService;
	@Inject private SessionUser sessionUser;
	@Inject private CollectionProvenanceService collectionService;
	@Inject private CommandService commandService;

	public void runCommand(Activity activity) {
//		Activity pActivity = find(activity.getId()); TODO USAR IDACTIVITY PARA RECUPERAR NOME DOS ARQUIVOS
		List<String> filenamesCommand = getNameFilesFromCommand(activity.getLinhaComando());
		
		String[] filenamesUsed = activity.getNomeArquivo().split(",");
		
		for (String filename : filenamesCommand) {
			for (String filenameused : filenamesUsed) {
				if (!filename.equals(filenameused)) {
					collectionService.save(new CollectionProvenance(filenameused));
				}
			}
		}
		commandService.executeCommand(activity, FileUtil.getPathToFile(sessionUser.getUser().getLogin()) );
	}
	
//	private File[] getNameFilesFromSource() {
//		File dir = new File(FileUtil.getPathToFile(sessionUser.getUser().getLogin()));
//		return dir.listFiles();
//	}

	private List<String> getNameFilesFromCommand(String command) {
		String[] tokes = command.split("\\s");
		
		List<String> filenames = new ArrayList<>();
		for (String token : tokes) {
			if (token.contains(".")) {
				for (String suffix : END_NAMES_FILE) {
					if (token.endsWith(suffix)) {
						filenames.add(token);
					}
				}
			}
		}
		return filenames;
	}
	
	public Long save(Activity activity) {
		Activity pActivity = find(activity.getId());
		if (pActivity == null) {
			Long idActivity = activityRepository.save(activity);
			if (activity.getNomeArquivo() != null) {
				List<CollectionProvenance> collections = getFile(activity.getNomeArquivo().split(","));
				
				collectionService.save(collections);
				activityRepository.saveRelationshipUsed(activity, collections);
			}
			return idActivity;
		} else {
			//			activityRepository.update(activity);
		}
		return -1l;
	}
	
	private List<CollectionProvenance> getFile(String[] filenames) {
		File dir = new File(FileUtil.getPathToFile(sessionUser.getUser().getLogin()));
		List<CollectionProvenance> collections = new ArrayList<>(); 
		
		for (String filename : filenames) {
			for (File file : dir.listFiles()) {
				if (file.getName().equals(filename)) {
					collections.add(new CollectionProvenance(file));
				}
			}
		}
		return collections;
	}

	public List<Activity> list(Account account) {
		return activityRepository.list(account);
	}

	public Activity find(long idActivity) {
		Activity activity = activityRepository.find(idActivity);
		if (activity != null) {
			activity.setAccount(accountService.findAccountByActivity(idActivity));
			activity.setGroup(groupService.findGroupByActivity(idActivity));
			return activity;
		}
		return null;
	}

	public void saveFiles(List<UploadedFile> listFiles) {
		for (UploadedFile uploadedFile : listFiles) {
			saveFile(uploadedFile);
		}
	}

	public void saveFile(UploadedFile uploadFile) {
		createFolderDst();
		String sourceDst = FileUtil.getPathToFile(sessionUser.getUser().getLogin())+ FILE_SEPARATOR + uploadFile.getFileName();
		saveFile(uploadFile, sourceDst);
	}

	private void saveFile(UploadedFile uploadFile, String sourceDst) {
		File file = new File(sourceDst);
		BufferedInputStream bufferedInputStream = null;
		FileOutputStream fileOutputStream = null;

		try {
			bufferedInputStream = new BufferedInputStream(uploadFile.getFile());
			fileOutputStream = new FileOutputStream(file);
			byte[] buffer = new byte[1024];  
			int count;  
			while ((count = bufferedInputStream.read(buffer)) > 0) {  
				fileOutputStream.write(buffer, 0, count);  
			}  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFolderDst() {
		FileUtil.createFolder(FileUtil.getPathToFile(sessionUser.getUser().getLogin()));
	}

}
