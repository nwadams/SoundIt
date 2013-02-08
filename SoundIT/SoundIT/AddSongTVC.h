//
//  AddSongTVC.h
//  SoundIT Canada
//
//  Created by Samuel Chan on 2013-02-07.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "API.h"
#import "AddSongCell.h"
#import "UIAlertView+error.h"

@interface AddSongTVC : UITableViewController <UIAlertViewDelegate>

@property NSArray *addSongListItems;

-(void)refreshAddSongList;
- (IBAction)didPressRefreshAddSongList:(id)sender;

@end