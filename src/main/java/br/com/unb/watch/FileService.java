package br.com.unb.watch;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import br.com.caelum.vraptor.ioc.Component;
import br.com.unb.model.Activity;
import br.com.unb.model.CollectionProvenance;
import br.com.unb.services.CollectionProvenanceService;

@Component
public class FileService {
	
    private File file;
    private Activity activity;
    private AtomicBoolean stop = new AtomicBoolean(false);
    @Inject private CollectionProvenanceService collectionService;

    public void setDirectory(File directory) {
    	this.file = directory;
    }
    
    public void setActivity(Activity activity) {
    	this.activity = activity;
    }
    
    public void doOnChange(Path newPath) {
        	String user = getUserFromFilePath();
        	collectionService.update(user, activity, new CollectionProvenance(newPath.toFile()));
    }

    private String getUserFromFilePath() {
    	String filePath = file.getAbsolutePath();
    	String user = filePath.substring(filePath.lastIndexOf("/")+1);
    	return user;
    }
    
    public class FileWatcher extends Thread {
    	public boolean isStopped() { return stop.get(); }
        public void stopThread() { stop.set(true); }

        public FileWatcher(File directory, Activity activity) {
        	setActivity(activity);
        	setDirectory(directory);
        }
        

    	@SuppressWarnings("unchecked")
    	@Override
        public void run() {
//    		We obtain the file system of the Path
    		Path path = file.toPath();
            FileSystem fs = path.getFileSystem();
            
            // We create the new WatchService using the new try() block
//            try (WatchService service = fs.newWatchService()) {
    		
            try (WatchService watcher = fs.newWatchService()) {
                path.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
                
                while (!isStopped()) {
                    WatchKey key;
                    try { 
                    	key = watcher.poll(25, TimeUnit.MILLISECONDS); 
                    }
                    catch (InterruptedException e) { 
                    	return; 
                    }
                    
                    if (key == null) { 
                    	Thread.yield(); 
                    	continue; 
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            Thread.yield();
                            continue;
                        } else if (kind == ENTRY_MODIFY) {
                        	// modified
                            Path newPath = ((WatchEvent<Path>) event).context();
                            
                        	System.out.println("New path modified: " + newPath);
                            doOnChange(newPath);
                        } else if (kind == ENTRY_CREATE) {
                        	Path newPath = ((WatchEvent<Path>) event).context();
                            
                        	System.out.println("New path created: " + newPath);
                            doOnChange(newPath);
                        }
                        boolean valid = key.reset();
                        if (!valid) { 
                        	break; 
                        }
                    }
                    Thread.yield();
                }
            } catch (Throwable e) {
                // Log or rethrow the error
            	e.printStackTrace();
            }
        }

    }
		
//	public static void main(String[] args) throws IOException,InterruptedException {
//		File dir = new File("/Users/rodrigopinheiro/Documents");
//		FileWatcher fileWatcher = new FileWatcher(dir);
//		fileWatcher.start();
////		watchDirectoryPath(dir.toPath());
//	}
}