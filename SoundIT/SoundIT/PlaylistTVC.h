//
//  PlaylistTVC.h
//  SoundIT
//
//  Created by Samuel Chan on 2013-01-13.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "API.h"
#import "PlaylistSongCell.h"
#import "UIAlertView+error.h"

@protocol PlaylistTVCDelegate <NSObject>

//nothing to do here; delegation setup to pass thisDeviceUniqueIdentifier around

@end

@interface PlaylistTVC : UITableViewController

@property (strong, nonatomic) NSArray *playlistItems;
@property (strong, nonatomic) NSString *thisDeviceUniqueIdentifier;
@property (strong, nonatomic) id <PlaylistTVCDelegate> delegate;

-(void)refreshPlaylist;

@end
