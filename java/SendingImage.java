protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            //if the request code is ok then we take the data uri of the image
            Uri imageUri = data.getData();
            //cropping the image taken from gallery to 1:1 ratio
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
//if the crop is done carefully then it is send below
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//taking the cropped image uri into result uri
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                String currentUserId = mFirebaseUser.getUid();

                Uri resultUri = result.getUri();

                File thumbFilePath = new File(resultUri.getPath());

               try {
                   //--//
                   Bitmap thumb_bitmap = new Compressor(this)
                           .setMaxHeight(200)
                           .setMaxWidth(200)
                           .setQuality(75)
                           .compressToBitmap(thumbFilePath);


                   ByteArrayOutputStream baos = new ByteArrayOutputStream();
                   thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                   final byte[] thumb_bytes = baos.toByteArray();
               }

               catch(IOException e)
            {
                e.printStackTrace();
            }
                // getting the file path of the storage in firebase storage into to file path
                final StorageReference filePath = mStorageReference.child("profile_image").child(currentUserId + ".jpg");

                final StorageReference thumbFile1 = mStorageReference.child("profile_image").child("thumbnails").child(currentUserId + ".jpg");


                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                        //getting the downlaod url
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull final Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downUri = task.getResult();
                            imageDownload = downUri.toString();


                            final UploadTask uploadTask = thumbFile1.putBytes(thumb_bytes);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }

                                            // Continue with the task to get the download URL
                                            return thumbFile1.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                                String ThumbUrl = downloadUri.toString();

                                                Map ImgData = new HashMap();
                                                ImgData.put("image", imageDownload);
                                                ImgData.put("thumb_image", ThumbUrl);
                                               


                                                mDatabaseReference.updateChildren(ImgData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        if (aVoid != null) {
                                                            Toast.makeText(getApplicationContext(), "picupdates in datavbase", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "Error in thumbnail", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });


                                            } else {


                                            }
                                        }
                                    });

                                }
                            });


                        }
                    }
                });


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
