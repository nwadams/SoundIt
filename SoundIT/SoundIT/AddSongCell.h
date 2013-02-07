//
//  AddSongCell.h
//  SoundIT Canada
//
//  Created by Samuel Chan on 2013-02-07.
//  Copyright (c) 2013 SoundIT. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AddSongCell : UITableViewCell
@property (strong, nonatomic) IBOutlet UILabel *songName;
@property (strong, nonatomic) IBOutlet UILabel *artistName;
@property (strong, nonatomic) NSMutableString *music_track_id;

@end
