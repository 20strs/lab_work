#include <stdio.h>
#include <stdlib.h>

typedef struct elem{
        int k,v;
	struct elem* next;
}elem;

int hash(int k,int m){
	return k%m;
}

elem* insert_before_head(elem* x,int k,int v){
	elem* new=(elem*)calloc(1,sizeof(elem));
	new->k=k,new->v=v;
	new->next=x;
	return new;
}

elem* list_search(elem* l,int k){
	elem* x=l;
	
	while(x&&(x->k!=k))
		x=x->next;
	return x;
}

int lookup(elem** h_t,int m,int key){
	int i=hash(key,m);
	elem* x=list_search(h_t[i],key);
	
	return x==NULL ? 0 : x->v;
}

void assign(elem** h_t,int m,int key,int v){
	int i=hash(key,m);
	elem* x=list_search(h_t[i],key);
	
	if(!x) h_t[i]=insert_before_head(h_t[i],key,v);
	else x->v=v;
}

elem* delete_list(elem* l){
	elem* x=l;
	
	while(x){
		l=l->next;
		free(x);
		x=l;
	}
	return NULL;
}

void free_hash_t(elem** h_t,int m){
	for(int i=0;i<m;i++)
		if(h_t[i]) h_t[i]=delete_list(h_t[i]);
}

int main(){
	int n,m,key,v;
	scanf("%d %d",&n,&m);
	
	elem** hash_t=(elem**)calloc(m,sizeof(elem*));
	
	for(int i=0;i<n;i++){
		char buf[8];
		scanf("%s %d",buf,&key);
		
		if(buf[1]=='S'){
			scanf("%d",&v);
			assign(hash_t,m,key,v);
		}
		if(buf[1]=='T') 
			printf("%d\n",lookup(hash_t,m,key));	
	}
	free_hash_t(hash_t,m);
	free(hash_t);
	
	return 0;
} 
