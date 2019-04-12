 public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView displayName;
        public TextView displayTime;
        public MessageViewHolder(final View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.message_text_layout);
            displayName = (TextView) itemView.findViewById(R.id.name_text_layout);
            displayTime = (TextView) itemView.findViewById(R.id.time_text_layout);
            profileImage = (CircleImageView) itemView.findViewById(R.id.message_profile_layout);
            // messageImage = (ImageView)itemView.findViewById(R.id.message_image_layout);

            context = itemView.getContext();

            //---DELETE FUNCTION---
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    CharSequence options[] = new CharSequence[]{"Delete", "Cancel"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete this message");
                    builder.setItems(options, new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (which == 0) {
                                /*
                                        ....CODE FOR DELETING THE MESSAGE IS YET TO BE WRITTEN HERE...
                                 */
                                String current = mAuth.getCurrentUser().getUid();
                                DatabaseReference dmsg1 = FirebaseDatabase.getInstance().getReference("messages");
                                DatabaseReference dmsg2 = FirebaseDatabase.getInstance().getReference("messages");
                                dmsg1 = dmsg1.child(current).child(mChatUser);
                                dmsg2 = dmsg2.child(mChatUser).child(current);
                                String keyMsg = (String) messageText.getText();
                                Query query = dmsg1.orderByChild("message").equalTo(keyMsg);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot mySnapshot : dataSnapshot.getChildren()) {
                                            mySnapshot.getRef().removeValue();
                                            itemView.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e(TAG, "onCancelled", databaseError.toException());


                                    }
                                });
                                Query query1 = dmsg2.orderByChild("message").equalTo(keyMsg);
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot mySnapshot : dataSnapshot.getChildren()) {
                                            mySnapshot.getRef().removeValue();
                                            itemView.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e(TAG, "onCancelled", databaseError.toException());
                                    }
                                });
                            }

                            if (which == 1) {

                            }

                        }
                    });
                    builder.show();

                    return true;
                }
            });
