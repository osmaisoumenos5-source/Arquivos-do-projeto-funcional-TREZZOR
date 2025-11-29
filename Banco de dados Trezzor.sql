-- Criar o banco de dados

drop DATABASE MercadoTrezzor;
GO
INSERT INTO Usuarios (Nome, Email, Senha, TipoUsuario)
VALUES ('Admin', 'admin@trezzor.com', '123456', 'Administrador');
USE MercadoTrezzor;
GO

-- Tabela de Usuários
CREATE TABLE Usuarios (
    UsuarioID INT PRIMARY KEY IDENTITY(1,1),
    Nome NVARCHAR(100),
    Email NVARCHAR(100) UNIQUE,
    Senha NVARCHAR(100),
    DataCadastro DATETIME DEFAULT GETDATE(),
    TipoUsuario NVARCHAR(50)
);

-- Tabela de Itens
CREATE TABLE Itens (
    ItemID INT PRIMARY KEY IDENTITY(1,1),
    Nome NVARCHAR(100),
    Descricao NVARCHAR(MAX),
    Categoria NVARCHAR(50),
    Ano INT,
    ImagemURL NVARCHAR(255),
    ValorEstimado DECIMAL(10,2)
);

-- Tabela de Anúncios
CREATE TABLE Anuncios (
    AnuncioID INT PRIMARY KEY IDENTITY(1,1),
    ItemID INT FOREIGN KEY REFERENCES Itens(ItemID),
    UsuarioID INT FOREIGN KEY REFERENCES Usuarios(UsuarioID),
    Preco DECIMAL(10,2),
    DataPublicacao DATETIME DEFAULT GETDATE(),
    Status NVARCHAR(50)
);

-- Tabela de Transações
CREATE TABLE Transacoes (
    TransacaoID INT PRIMARY KEY IDENTITY(1,1),
    AnuncioID INT FOREIGN KEY REFERENCES Anuncios(AnuncioID),
    CompradorID INT FOREIGN KEY REFERENCES Usuarios(UsuarioID),
    DataVenda DATETIME,
    ValorFinal DECIMAL(10,2)
);

-- Tabela de Histórico de Preços
CREATE TABLE HistoricoDePrecos (
    HistoricoID INT PRIMARY KEY IDENTITY(1,1),
    ItemID INT FOREIGN KEY REFERENCES Itens(ItemID),
    Data DATETIME,
    Preco DECIMAL(10,2)
);

-- Tabela de Comentários
CREATE TABLE Comentarios (
    ComentarioID INT PRIMARY KEY IDENTITY(1,1),
    UsuarioID INT FOREIGN KEY REFERENCES Usuarios(UsuarioID),
    ItemID INT FOREIGN KEY REFERENCES Itens(ItemID),
    Texto NVARCHAR(MAX),
    DataComentario DATETIME DEFAULT GETDATE()
);

--Principais Entidades e Funções


--Usuários;
--Armazena dados de quem usa a plataforma.
--Campos: UsuarioID, Nome, Email, Senha, DataCadastro, TipoUsuario.

--Itens:
--Representa os produtos raros.
--Campos: ItemID, Nome, Descrição, Categoria, Ano, ImagemURL, ValorEstimado.

--Anúncios:
--Cada anúncio é vinculado a um item e a um usuário.
--Campos: AnuncioID, ItemID, UsuarioID, Preço, DataPublicacao, Status.

--Transações:
--Registra vendas concluídas.
--Campos: TransacaoID, AnuncioID, CompradorID, DataVenda, ValorFinal.



--HistóricoDePreços:
--Guarda variações de preço ao longo do tempo.
--Campos: HistoricoID, ItemID, Data, Preço.

--Comentários:
--Permite interação entre usuários sobre itens.
--Campos: ComentarioID, UsuarioID, ItemID, Texto, DataComentario.

--Relacionamentos:
--Usuário ↔ Anúncio: Um usuário pode criar vários anúncios.
--Item ↔ Anúncio: Um item pode aparecer em vários anúncios.
--Anúncio ↔ Transação: Cada transação está ligada a um anúncio.
--Item ↔ HistóricoDePreços: Um item pode ter vários registros de preço.
--Usuário ↔ Comentário: Um usuário pode comentar sobre vários itens.
