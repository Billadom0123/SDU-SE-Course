function [filename] = image_save(filename,target_dir,format)
    disp(filename)
    
    filename = fullfile(target_dir,filename);
    
    % 确保目录存在  
    dir_path = fileparts(filename);  
    if ~exist(dir_path, 'dir')    
        mkdir(dir_path);  % 只创建目录，不创建文件  
    end
    
    disp(filename)
    saveas(gcf,filename,format)
end

