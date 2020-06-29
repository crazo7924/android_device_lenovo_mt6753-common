/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef __LIBNVRAM_WRAPPER_H
#define __LIBNVRAM_WRAPPER_H

#include <fstab.h>

#ifdef __cplusplus
extern "C" {
#include <cstdlib>
#endif
	#include <stdbool.h>
	#include <stdint.h>
	#include <stdio.h>	
	
	/*
	 * The entries must be kept in the same order as they were seen in the fstab.
	 * Unless explicitly requested, a lookup on mount point should always
	 * return the 1st one.
	 */
	struct fstab {
	    int num_entries;
	    struct fstab_rec* recs;
	    char* fstab_filename;
	};

	struct fstab_rec {
	    char* blk_device;
	    char* mount_point;
	    char* fs_type;
	    unsigned long flags;
	    char* fs_options;
	    int fs_mgr_flags;
	    char* key_loc;
	    char* key_dir;
	    char* verity_loc;
	    long long length;
	    char* label;
	    int partnum;
	    int swap_prio;
	    int max_comp_streams;
	    unsigned int zram_size;
	    uint64_t reserved_size;
	    unsigned int file_contents_mode;
	    unsigned int file_names_mode;
	    unsigned int erase_blk_size;
	    unsigned int logical_blk_size;
	    char* sysfs_path;
	};

	
	struct fstab* fs_mgr_read_fstab(const char* fstab_path);
	void fs_mgr_free_fstab(struct fstab* fstab);
	struct fstab_rec* fs_mgr_get_entry_for_mount_point(struct fstab* fstab, const char* path);
}

using namespace android::fs_mgr;

typedef union {
	FstabEntry::FsMgrFlags flags;
	int i;
} FsMgrFlagsConvertor;

struct fstab* toC_struct(Fstab* fsTab, std::string fstab_path);

struct fstab* toC_struct(Fstab* fsTab, std::string fstab_path)
{
	FstabEntry *entry = &(fsTab->at(0));
	struct fstab* f = (struct fstab*)malloc(sizeof(struct fstab));
	
	struct fstab_rec* r = (struct fstab_rec*)malloc(sizeof(struct fstab_rec));
	r->blk_device = (char*)(entry->blk_device.c_str());
	r->mount_point = (char*)(entry->mount_point.c_str());
	r->fs_type = (char*)(entry->fs_type.c_str());
	r->flags = entry->flags;
        r->fs_options = (char*)(entry->fs_options.c_str());
        
	FsMgrFlagsConvertor convertor;
        convertor.flags	= entry->fs_mgr_flags;
	r->fs_mgr_flags = convertor.i;

	r->key_loc = (char*)(entry->key_loc.c_str());
        r->key_dir = (char*)(entry->key_dir.c_str());
        r->verity_loc = (char*)(entry->verity_loc.c_str());
	r->length = entry->length;
        r->label = (char*)(entry->label.c_str());
        r->partnum = entry->partnum;
	r->swap_prio = entry->swap_prio;
	r->max_comp_streams = entry->max_comp_streams;
	r->zram_size = entry->zram_size;
	r->reserved_size = entry->reserved_size;
	r->file_contents_mode = atoi((char*)(entry->file_contents_mode.c_str()));
	r->file_names_mode = atoi((char*)(entry->file_names_mode.c_str()));
	r->erase_blk_size = entry->erase_blk_size;
	r->logical_blk_size = entry->logical_blk_size;
	r->sysfs_path = (char*)(entry->sysfs_path.c_str());

	f->fstab_filename = (char*)(fstab_path.substr(fstab_path.find_last_of("/\\") + 1).c_str());
	f->num_entries = fsTab->size();
	f->recs = r;

	return f;
}

Fstab* toCpp_struct(struct fstab_rec* fstab);

Fstab* toCpp_struct(struct fstab_rec* fstab)
{
        FstabEntry entry;
        entry.blk_device = std::string(fstab->blk_device);
        entry.mount_point = std::string(fstab->mount_point);
        entry.fs_type = std::string(fstab->fs_type);
        entry.flags = fstab->flags;
        entry.fs_options = std::string(fstab->fs_options);
        
	FsMgrFlagsConvertor convertor;
	convertor.i = fstab->fs_mgr_flags;
	entry.fs_mgr_flags = convertor.flags;
        
	entry.key_loc = std::string(fstab->key_loc);
        entry.key_dir = std::string(fstab->key_dir);
        entry.verity_loc = std::string(fstab->verity_loc);
        entry.length = fstab->length;
        entry.label = std::string(fstab->label);
        entry.partnum = fstab->partnum;
        entry.swap_prio = fstab->swap_prio;
        entry.max_comp_streams = fstab->max_comp_streams;
        entry.zram_size = fstab->zram_size;
        entry.reserved_size = fstab->reserved_size;
	entry.file_contents_mode = fstab->file_contents_mode;
        entry.file_names_mode = fstab->file_names_mode;
        entry.erase_blk_size = fstab->erase_blk_size;
        entry.logical_blk_size = fstab->logical_blk_size;
        entry.sysfs_path = std::string(fstab->sysfs_path);

        Fstab *fsTab;
        fsTab->push_back(entry);

	return fsTab;
}

struct fstab* fs_mgr_read_fstab(const char* fstab_path)
{
	Fstab* fsTab = nullptr;
	bool success = ReadFstabFromFile(std::string(fstab_path), fsTab);
       	
	if(!success) {
		return NULL;
	}

	return toC_struct(fsTab, std::string(fstab_path));
}


void fs_mgr_free_fstab(struct fstab* fstab)
{
	free( fstab );
}

struct fstab_rec* fs_mgr_get_entry_for_mount_point(struct fstab* fstab, const char* path)
{
	Fstab *fsTab;
	fsTab->push_back(*GetEntryForMountPoint(toCpp_struct(fstab->recs), std::string(path)));

	return toC_struct(fsTab, std::string(path))->recs;
}
#endif // __LIBNVRAM_WRAPPER_H
