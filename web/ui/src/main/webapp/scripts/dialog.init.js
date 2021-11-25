/**
 * http://usejsdoc.org/
 */

function formattingDialog(dialog){
	if (dialog && typeof dialog === 'object'){
		dialog = createObjectDialog(dialog.title, dialog.size, dialog.draggable, dialog.closable)
	}else{
		dialog = createObjectDialog()
	}
	return dialog
}

function createObjectDialog(title = ' ', size = undefined, dragg = true, closable = false){
	var dialog = {}
	dialog.title = title;
	dialog.size = getDialogSize(size);
	dialog.draggable = dragg;
	dialog.closable = closable;
	return dialog;
}

function getDialogSize(size = ''){
	var dialogSize
	switch (size.toLowerCase()){
	case 'wide':
	case 'size-wide': dialogSize = BootstrapDialog.SIZE_WIDE 
		break;
	case 'small':
	case 'size-small': dialogSize = BootstrapDialog.SIZE_SMALL
		break;
	case 'large':
	case 'size-large': dialogSize = BootstrapDialog.SIZE_LARGE
		break;
	default: 
		dialogSize = BootstrapDialog.SIZE_NORMAL;
	}
	return dialogSize;
}
