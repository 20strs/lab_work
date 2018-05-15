#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

typedef struct node{
        int v,k,balance;
	struct node* left,*right,*parent;
}node;

node* descend(node* t,int k){
	node* x=t;
	while(x&&(x->k!=k)){	
		if(k<x->k) x=x->left;
		else x=x->right;
	}
	return x;
}

node *minimum(node* t){
	node* x;
	if (!t)
		x=NULL;
	else{
		x=t;
		while (x->left)
			x=x->left;
	}
	return x;	
}

int compare_node(node* a,node* b){
	if((!a)|(!b)) return 0;
	return ((a->k==b->k)&&(a->v==b->v)) ? 1 : 0;
}

node* succ(node* x){
	node* y;
	if(x->right) y=minimum(x->right);
	else{
		y=x->parent;
		while(y&&compare_node(x,y->right)){
			x=y;
			y=y->parent;
		}
	}
	return y;
}

node* insert(node* t,node* new,int k,int v){
	node* x;
	new->k=k,new->v=v;
	new->parent=new->left=new->right=NULL;
	if(!t) t=new;
	else{
		x=t;
		for(;;){
			if(k<x->k){
				if(!x->left){
					x->left=new;
					new->parent=x;
					break;
				}
				x=x->left;
			}
			else{ 
				if(!x->right){
					x->right=new;
					new->parent=x;
					break;
				}
				x=x->right;
			}
		}
	}
	return t;
}

node* replace_node(node* t,node* x,node* y){
	if(compare_node(x,t)){
		t=y;
		if(y) y->parent=NULL;
	}
	else{
			node* p=x->parent;
					
			if(y) y->parent=p;
			
			if(compare_node(x,p->left)) p->left=y;
			else p->right=y;
	}
	return t;
}

node* rotate_left(node* t,node*x){
	node* y,*b;
	y=x->right;
	
	t=replace_node(t,x,y);
	b=y->left;
	if(b) b->parent=x;
	x->right=b;
	x->parent=y;
	y->left=x;
	
	--x->balance;
	if(y->balance>0) x->balance-=y->balance;
	--y->balance;
	if(x->balance<0) y->balance+=x->balance;
	return t;
}
node* rotate_right(node* t,node* x){
	node* y,*b;
	y=x->left;
	
	t=replace_node(t,x,y);
	b=y->right;
	if(b) b->parent=x;
	x->left=b;
	x->parent=y;
	y->right=x;
	
	++x->balance;
	if(y->balance<0) x->balance-=y->balance;
	++y->balance;
	if(x->balance>0) y->balance+=x->balance;
	return t;
}

node* insert_avl(node* t,int k,int v){
	node* a=(node*)malloc(sizeof(node));
	t = insert(t,a,k,v);
	a->balance=0;
	for(;;){
		node* x=a->parent;
		if (!x)	break;
		if(compare_node(a,x->left)){
			--x->balance;
			if(x->balance==0) break;
			if(x->balance==-2){
				if(a->balance==1) t=rotate_left(t,a);
				t=rotate_right(t,x);
				break;
			}
		}else{
			++x->balance;
			if(x->balance==0) break;
			if(x->balance==2){
				if(a->balance==-1) t=rotate_right(t,a);
				t=rotate_left(t,x);
				break;
			}
		}
		a=x;
	}
	return t;
}

int HashH37(const char * str){
	int hash = 2139062143;
	for(int i=0,len=strlen(str);i<len;i++)
		hash = 37 * hash + str[i];
	return hash;
}

void delete_tree(node* t){
	if(t==NULL) return;
	delete_tree(t->left);
	delete_tree(t->right);
	free(t);
}

int main(){
	int n,i,id;
	scanf("%d\n",&n);
	node* tree=NULL;
	char* s=(char*)calloc(n+1,1);
	gets(s);
	for (i=id=0; i<n;i++){
		char c=s[i];
		if(c==' ') continue;
		if (isalpha(c)){
			char *buf=(char*)calloc(33,1);
			for(int k=i;isalnum(s[i]);i++)
				buf[i-k]=s[i];
			i--;
			int key=HashH37(buf);
			if(!descend(tree,key)){
				tree = insert_avl(tree,key,id);
				printf("IDENT %d\n",id++);
			}
			else printf("IDENT %d\n",descend(tree,key)->v);
			free(buf); 
		}
		else if(isdigit(c)){
				char *buf=(char*)calloc(50,1);
				int k=i;
				while(isdigit(s[i])){
					buf[i-k]=s[i];
					i++;
				}
				i--;
				printf("CONST %s\n",buf);
				free(buf);
			}
		else {
			switch(c){
				case '+': printf("SPEC 0\n");
							break;
				case '-': printf("SPEC 1\n");
							break;
				case '*': printf("SPEC 2\n");
							break;
				case '/': printf("SPEC 3\n");
							break;
				case '(': printf("SPEC 4\n");
							break;
				case ')': printf("SPEC 5\n");
							break;
			}
		}
	}
	delete_tree(tree),free(s);
	return 0;
}

